import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-error-state',
  standalone: true,
  template: `
    <div class="card border-red-400/45 bg-red-950/35 p-6 text-center" [attr.data-testid]="testId()">
      <p class="text-lg font-bold uppercase tracking-[0.12em] text-red-200">{{ title() }}</p>
      <p class="mt-2 text-sm text-red-100/90">{{ message() }}</p>
      <button class="btn-secondary mt-4" type="button" (click)="retry.emit()">Reintentar</button>
    </div>
  `
})
export class ErrorStateComponent {
  readonly title = input('Ocurrio un error');
  readonly message = input('No se pudo completar la operacion.');
  readonly testId = input('error-state');
  readonly retry = output<void>();
}
