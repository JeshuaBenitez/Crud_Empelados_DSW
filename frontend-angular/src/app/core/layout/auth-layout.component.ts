import { Component, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-auth-layout',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <div class="min-h-screen text-slate-100">
      <header class="sticky top-0 z-40 border-b border-white/10 bg-slate-950/80 backdrop-blur">
        <div class="mx-auto flex max-w-7xl items-center justify-between px-4 py-4">
          <div>
            <p class="module-subtitle text-red-200">DW Gabo Racing Data</p>
            <h1 class="text-lg font-bold uppercase tracking-[0.14em] text-slate-100">Gestion de Personal</h1>
          </div>
          <button class="btn-danger" type="button" (click)="logout()" data-testid="logout-button">Cerrar sesion</button>
        </div>
      </header>

      <div class="mx-auto max-w-7xl px-4 py-6">
        <main class="pb-8">
          <router-outlet />
        </main>
      </div>
    </div>
  `
})
export class AuthLayoutComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
