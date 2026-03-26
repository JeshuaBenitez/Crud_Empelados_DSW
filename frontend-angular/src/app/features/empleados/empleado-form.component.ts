import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { EmpleadosService } from './empleados.service';
import { NotificationService } from '../../shared/services/notification.service';

@Component({
  selector: 'app-empleado-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <section class="space-y-4" data-testid="empleado-form-page">
      <div class="card border-red-500/25 p-4">
        <p class="module-subtitle text-red-200">Ficha Tecnica</p>
        <h2 class="module-title mt-1">{{ isEditMode() ? 'Editar empleado' : 'Nuevo empleado' }}</h2>
        <p class="mt-1 text-sm text-slate-300">Completa los datos requeridos para guardar.</p>
      </div>

      <form class="card space-y-4 border-white/15 p-6" [formGroup]="form" (ngSubmit)="submit()" data-testid="empleado-form">
        <div>
          <label class="mb-1 block text-sm font-semibold uppercase tracking-[0.12em] text-slate-200" for="nombre">Nombre</label>
          <input id="nombre" class="field" formControlName="nombre" />
        </div>

        <div>
          <label class="mb-1 block text-sm font-semibold uppercase tracking-[0.12em] text-slate-200" for="direccion">Direccion</label>
          <input id="direccion" class="field" formControlName="direccion" />
        </div>

        <div>
          <label class="mb-1 block text-sm font-semibold uppercase tracking-[0.12em] text-slate-200" for="telefono">Telefono</label>
          <input id="telefono" class="field" formControlName="telefono" />
        </div>

        @if (error()) {
          <p class="rounded-lg border border-red-400/50 bg-red-950/45 px-3 py-2 text-sm text-red-200">{{ error() }}</p>
        }

        <div class="flex gap-2">
          <button class="btn-primary" type="submit" [disabled]="saving()">{{ saving() ? 'Guardando...' : 'Guardar' }}</button>
          <a class="btn-secondary" routerLink="/empleados">Cancelar</a>
        </div>
      </form>
    </section>
  `
})
export class EmpleadoFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly empleadosService = inject(EmpleadosService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly notificationService = inject(NotificationService);

  readonly saving = signal(false);
  readonly error = signal<string | null>(null);
  readonly isEditMode = signal(false);
  private currentClave: string | null = null;

  readonly form = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    direccion: ['', [Validators.required, Validators.maxLength(100)]],
    telefono: ['', [Validators.required, Validators.maxLength(100)]]
  });

  constructor() {
    const clave = this.route.snapshot.paramMap.get('clave');
    if (!clave) {
      return;
    }

    this.isEditMode.set(true);
    this.currentClave = clave;

    this.empleadosService.getByClave(clave).subscribe({
      next: (empleado) => {
        this.form.patchValue({
          nombre: empleado.nombre,
          direccion: empleado.direccion,
          telefono: empleado.telefono
        });
      },
      error: () => this.error.set('No se pudo cargar el empleado solicitado.')
    });
  }

  submit(): void {
    if (this.form.invalid || this.saving()) {
      this.form.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.error.set(null);

    const request$ = this.isEditMode() && this.currentClave
      ? this.empleadosService.update(this.currentClave, this.form.getRawValue())
      : this.empleadosService.create(this.form.getRawValue());

    request$.subscribe({
      next: () => {
        this.notificationService.show('Empleado guardado correctamente.', 'success');
        this.router.navigate(['/empleados']);
      },
      error: (err) => {
        if (err?.status === 403) {
          this.error.set('El backend no permite esta operacion para empleados en este contexto.');
        } else {
          this.error.set('No fue posible guardar el empleado.');
        }
        this.saving.set(false);
      },
      complete: () => this.saving.set(false)
    });
  }
}
