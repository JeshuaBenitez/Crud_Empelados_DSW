import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';
import { NotificationService } from '../../shared/services/notification.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="relative mx-auto flex min-h-screen max-w-5xl items-center px-4 py-8">
      <div class="pointer-events-none absolute inset-0 bg-[radial-gradient(circle_at_15%_15%,rgba(245,26,45,0.32),transparent_36%),radial-gradient(circle_at_85%_85%,rgba(198,165,97,0.2),transparent_36%)]"></div>

      <section class="card relative mx-auto w-full max-w-md border-red-500/25 p-8" data-testid="login-card">
        <p class="module-subtitle text-red-200">Pitwall Access</p>
        <h2 class="mt-2 text-3xl font-black uppercase tracking-[0.12em] text-slate-100">Iniciar sesion</h2>
        <p class="mt-2 text-sm text-slate-300">Accede al panel operativo con tu cuenta de empleado.</p>

        <form class="mt-6 space-y-4" [formGroup]="form" (ngSubmit)="submit()" data-testid="login-form">
          <div>
            <label class="mb-1 block text-sm font-semibold uppercase tracking-[0.12em] text-slate-200" for="correo">Correo</label>
            <input id="correo" class="field" type="email" formControlName="correo" data-testid="login-email" />
          </div>

          <div>
            <label class="mb-1 block text-sm font-semibold uppercase tracking-[0.12em] text-slate-200" for="password">Password</label>
            <input id="password" class="field" type="password" formControlName="password" data-testid="login-password" />
          </div>

          @if (error()) {
            <p class="rounded-lg border border-red-400/50 bg-red-950/45 px-3 py-2 text-sm text-red-200" data-testid="login-error">{{ error() }}</p>
          }

          <button class="btn-primary w-full" type="submit" [disabled]="loading()" data-testid="login-submit">
            {{ loading() ? 'Ingresando...' : 'Entrar' }}
          </button>
        </form>

        <div class="mt-6 border-t border-white/10 pt-4 text-xs uppercase tracking-[0.16em] text-slate-400">
          Backend JWT | Precision Mode
        </div>
      </section>
    </div>
  `
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly notificationService = inject(NotificationService);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]]
  });

  submit(): void {
    if (this.form.invalid || this.loading()) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.authService.login(this.form.getRawValue()).subscribe({
      next: () => {
        this.notificationService.show('Bienvenido al sistema.', 'success');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.error.set(err?.status === 401 ? 'Credenciales invalidas.' : 'No fue posible iniciar sesion.');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false)
    });
  }
}
