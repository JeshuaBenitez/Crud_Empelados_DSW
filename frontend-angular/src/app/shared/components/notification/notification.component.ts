import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../services/notification.service';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [CommonModule],
  template: `
    @if (message()) {
      <div class="fixed right-4 top-4 z-50 max-w-sm rounded-xl border border-white/15 px-4 py-3 text-sm text-white shadow-2xl backdrop-blur"
        [ngClass]="{
          'bg-gradient-to-r from-red-700 to-red-500': message()?.type === 'success',
          'bg-gradient-to-r from-slate-700 to-slate-600': message()?.type === 'info',
          'bg-gradient-to-r from-red-900 to-red-700': message()?.type === 'error'
        }"
        data-testid="notification-toast">
        <div class="flex items-start justify-between gap-3">
          <span class="font-semibold uppercase tracking-[0.08em]">{{ message()?.text }}</span>
          <button type="button" class="text-white/80 hover:text-white" (click)="close()">X</button>
        </div>
      </div>
    }
  `
})
export class NotificationComponent {
  private readonly notificationService = inject(NotificationService);
  readonly message = computed(() => this.notificationService.message());

  close(): void {
    this.notificationService.clear();
  }
}
