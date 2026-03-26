import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import { ApiService } from '../http/api.service';
import { LoginRequest, LoginResponse } from '../../shared/models/auth.models';

const TOKEN_KEY = 'frontend.jwt.token';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly api = inject(ApiService);
  private readonly tokenSignal = signal<string | null>(sessionStorage.getItem(TOKEN_KEY));

  readonly token = computed(() => this.tokenSignal());
  readonly isAuthenticated = computed(() => !!this.tokenSignal());

  login(payload: LoginRequest): Observable<void> {
    return this.http.post<LoginResponse>(`${this.api.baseUrl}/auth/empleados/login`, payload).pipe(
      tap((response) => {
        sessionStorage.setItem(TOKEN_KEY, response.accessToken);
        this.tokenSignal.set(response.accessToken);
      }),
      map(() => void 0)
    );
  }

  logout(): void {
    sessionStorage.removeItem(TOKEN_KEY);
    this.tokenSignal.set(null);
  }
}
