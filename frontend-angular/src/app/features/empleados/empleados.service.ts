import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from '../../core/http/api.service';
import { Empleado, EmpleadoPage, EmpleadoRequest } from '../../shared/models/empleado.models';

@Injectable({ providedIn: 'root' })
export class EmpleadosService {
  private readonly http = inject(HttpClient);
  private readonly api = inject(ApiService);

  list(page = 0, size = 10): Observable<EmpleadoPage> {
    return this.http.get<EmpleadoPage>(`${this.api.baseUrl}/empleados?page=${page}&size=${size}`);
  }

  getByClave(clave: string): Observable<Empleado> {
    return this.http.get<Empleado>(`${this.api.baseUrl}/empleados/${clave}`);
  }

  create(payload: EmpleadoRequest): Observable<Empleado> {
    return this.http.post<Empleado>(`${this.api.baseUrl}/empleados`, payload);
  }

  update(clave: string, payload: EmpleadoRequest): Observable<Empleado> {
    return this.http.put<Empleado>(`${this.api.baseUrl}/empleados/${clave}`, payload);
  }

  remove(clave: string): Observable<void> {
    return this.http.delete<void>(`${this.api.baseUrl}/empleados/${clave}`);
  }
}
