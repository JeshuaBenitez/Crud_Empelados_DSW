import { Component, input } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  template: `
    <div class="card border-white/15 p-6 text-center" [attr.data-testid]="testId()">
      <p class="text-lg font-bold uppercase tracking-[0.12em] text-slate-100">{{ title() }}</p>
      <p class="mt-2 text-sm text-slate-300">{{ message() }}</p>
    </div>
  `
})
export class EmptyStateComponent {
  readonly title = input('No hay resultados');
  readonly message = input('Aun no hay datos para mostrar.');
  readonly testId = input('empty-state');
}
