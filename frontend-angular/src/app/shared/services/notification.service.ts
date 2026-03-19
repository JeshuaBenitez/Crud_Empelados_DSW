import { Injectable, signal } from '@angular/core';

export type NotificationType = 'success' | 'error' | 'info';

export interface NotificationMessage {
  text: string;
  type: NotificationType;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  readonly message = signal<NotificationMessage | null>(null);

  show(text: string, type: NotificationType = 'info'): void {
    this.message.set({ text, type });
    window.setTimeout(() => this.clear(), 3500);
  }

  clear(): void {
    this.message.set(null);
  }
}
