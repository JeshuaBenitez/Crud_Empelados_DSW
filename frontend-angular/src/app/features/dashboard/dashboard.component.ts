import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  template: `
    <section class="space-y-4" data-testid="dashboard-page">
      <div class="grid gap-4 md:grid-cols-2">
        <article class="card border-red-500/20 p-6">
          <p class="module-subtitle text-slate-300">Modulo</p>
          <h3 class="mt-1 text-lg font-bold uppercase tracking-[0.14em] text-slate-100">Empleados</h3>
          <p class="mt-2 text-sm text-slate-300">Consulta y administra empleados.</p>
          <a routerLink="/empleados" class="btn-primary mt-4 inline-flex">Ir a empleados</a>
        </article>

        <article class="card border-red-500/20 p-6">
          <p class="module-subtitle text-slate-300">Modulo</p>
          <h3 class="mt-1 text-lg font-bold uppercase tracking-[0.14em] text-slate-100">Departamentos</h3>
          <p class="mt-2 text-sm text-slate-300">Consulta y administra departamentos.</p>
          <a routerLink="/departamentos" class="btn-primary mt-4 inline-flex">Ir a departamentos</a>
        </article>
      </div>
    </section>
  `
})
export class DashboardComponent {}
