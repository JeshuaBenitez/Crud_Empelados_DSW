import { Component, input } from '@angular/core';

@Component({
  selector: 'app-loading-state',
  standalone: true,
  template: `
    <div class="card border-white/15 p-6 text-center" [attr.data-testid]="testId()">
      <div class="mx-auto mb-3 h-9 w-9 animate-spin rounded-full border-4 border-red-200/30 border-t-red-400"></div>
      <p class="text-xs uppercase tracking-[0.18em] text-slate-300">{{ text() }}</p>
    </div>
  `
})
export class LoadingStateComponent {
  readonly text = input('Cargando informacion...');
  readonly testId = input('loading-state');
}
