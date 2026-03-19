import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Departamento } from '../../shared/models/departamento.models';
import { DepartamentosService } from './departamentos.service';
import { LoadingStateComponent } from '../../shared/components/loading-state/loading-state.component';
import { ErrorStateComponent } from '../../shared/components/error-state/error-state.component';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { NotificationService } from '../../shared/services/notification.service';

@Component({
  selector: 'app-departamentos-list',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingStateComponent, ErrorStateComponent, EmptyStateComponent],
  template: `
    <section class="space-y-4" data-testid="departamentos-page">
      <div class="card border-red-500/25 flex flex-wrap items-center justify-between gap-3 p-4">
        <div>
          <p class="module-subtitle text-red-200">Linea Estructural</p>
          <h2 class="module-title mt-1">Departamentos</h2>
          <p class="mt-1 text-sm text-slate-300">Listado de departamentos del backend.</p>
        </div>
        <a class="btn-primary" routerLink="/departamentos/nuevo" data-testid="departamentos-create-link">Nuevo departamento</a>
      </div>

      @if (loading()) {
        <app-loading-state text="Cargando departamentos..." />
      } @else if (error()) {
        <app-error-state [message]="error()!" (retry)="load()" />
      } @else if (!departamentos().length) {
        <app-empty-state title="Sin departamentos" message="Crea el primer departamento para comenzar." />
      } @else {
        <div class="table-shell p-4">
          <table class="min-w-full text-sm" data-testid="departamentos-table">
            <thead>
              <tr class="table-head">
                <th class="px-3 py-2">Clave</th>
                <th class="px-3 py-2">Nombre</th>
                <th class="px-3 py-2 text-right">Acciones</th>
              </tr>
            </thead>
            <tbody>
              @for (departamento of departamentos(); track departamento.clave) {
                <tr class="table-row">
                  <td class="table-cell-code px-3 py-2">{{ departamento.clave }}</td>
                  <td class="px-3 py-2">{{ departamento.nombre }}</td>
                  <td class="px-3 py-2 text-right">
                    <a class="btn-secondary mr-2" [routerLink]="['/departamentos', departamento.clave, 'editar']">Editar</a>
                    <button class="btn-danger" type="button" (click)="delete(departamento)">Eliminar</button>
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
export class DepartamentosListComponent {
  private readonly departamentosService = inject(DepartamentosService);
  private readonly notificationService = inject(NotificationService);

  readonly loading = signal(true);
  readonly error = signal<string | null>(null);
  readonly departamentos = signal<Departamento[]>([]);

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);

    this.departamentosService.list().subscribe({
      next: (response) => this.departamentos.set(response.content ?? []),
      error: () => this.error.set('No se pudo cargar el listado de departamentos.'),
      complete: () => this.loading.set(false)
    });
  }

  delete(departamento: Departamento): void {
    const confirmed = window.confirm(`Eliminar departamento ${departamento.nombre}?`);
    if (!confirmed) {
      return;
    }

    this.departamentosService.remove(departamento.clave).subscribe({
      next: () => {
        this.notificationService.show('Departamento eliminado.', 'success');
        this.load();
      },
      error: (err) => {
        const message = err?.status === 409
          ? 'No se puede eliminar: existen empleados asociados.'
          : 'No fue posible eliminar el departamento.';
        this.notificationService.show(message, 'error');
      }
    });
  }
}
