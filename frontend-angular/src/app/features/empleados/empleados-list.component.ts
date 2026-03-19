import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { EmpleadosService } from './empleados.service';
import { Empleado } from '../../shared/models/empleado.models';
import { LoadingStateComponent } from '../../shared/components/loading-state/loading-state.component';
import { ErrorStateComponent } from '../../shared/components/error-state/error-state.component';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { NotificationService } from '../../shared/services/notification.service';

@Component({
  selector: 'app-empleados-list',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingStateComponent, ErrorStateComponent, EmptyStateComponent],
  template: `
    <section class="space-y-4" data-testid="empleados-page">
      <div class="card border-red-500/25 flex flex-wrap items-center justify-between gap-3 p-4">
        <div>
          <p class="module-subtitle text-red-200">Linea Operativa</p>
          <h2 class="module-title mt-1">Empleados</h2>
          <p class="mt-1 text-sm text-slate-300">Listado de empleados del backend.</p>
        </div>
        <a class="btn-primary" routerLink="/empleados/nuevo" data-testid="empleados-create-link">Nuevo empleado</a>
      </div>

      @if (loading()) {
        <app-loading-state text="Cargando empleados..." />
      } @else if (error()) {
        <app-error-state [message]="error()!" (retry)="load()" />
      } @else if (!empleados().length) {
        <app-empty-state title="Sin empleados" message="Crea un empleado para empezar." />
      } @else {
        <div class="table-shell p-4">
          <table class="min-w-full text-sm" data-testid="empleados-table">
            <thead>
              <tr class="table-head">
                <th class="px-3 py-2">Clave</th>
                <th class="px-3 py-2">Nombre</th>
                <th class="px-3 py-2">Telefono</th>
                <th class="px-3 py-2">Departamento</th>
                <th class="px-3 py-2 text-right">Acciones</th>
              </tr>
            </thead>
            <tbody>
              @for (empleado of empleados(); track empleado.clave) {
                <tr class="table-row">
                  <td class="table-cell-code px-3 py-2">{{ empleado.clave }}</td>
                  <td class="px-3 py-2">{{ empleado.nombre }}</td>
                  <td class="px-3 py-2">{{ empleado.telefono }}</td>
                  <td class="px-3 py-2 text-slate-300">{{ empleado.departamentoNombre || '-' }}</td>
                  <td class="px-3 py-2 text-right">
                    <a class="btn-secondary mr-2" [routerLink]="['/empleados', empleado.clave, 'editar']">Editar</a>
                    <button class="btn-danger" type="button" (click)="delete(empleado)">Eliminar</button>
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      }
    </section>
  `
})
export class EmpleadosListComponent {
  private readonly empleadosService = inject(EmpleadosService);
  private readonly notificationService = inject(NotificationService);

  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly empleados = signal<Empleado[]>([]);

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);

    this.empleadosService.list().subscribe({
      next: (response) => this.empleados.set(response.content ?? []),
      error: () => this.error.set('No se pudo cargar el listado de empleados.'),
      complete: () => this.loading.set(false)
    });
  }

  delete(empleado: Empleado): void {
    const confirmed = window.confirm(`Eliminar empleado ${empleado.nombre}?`);
    if (!confirmed) {
      return;
    }

    this.empleadosService.remove(empleado.clave).subscribe({
      next: () => {
        this.notificationService.show('Empleado eliminado correctamente.', 'success');
        this.load();
      },
      error: (err) => {
        const message = err?.status === 403
          ? 'El backend no permite eliminar este empleado en el contexto actual.'
          : 'No fue posible eliminar el empleado.';
        this.notificationService.show(message, 'error');
      }
    });
  }
}
