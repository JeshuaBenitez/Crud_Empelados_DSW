import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from '../../core/http/api.service';
import { Departamento, DepartamentoPage, DepartamentoRequest } from '../../shared/models/departamento.models';

@Injectable({ providedIn: 'root' })
export class DepartamentosService {
  private readonly http = inject(HttpClient);
  private readonly api = inject(ApiService);

  list(page = 0, size = 10): Observable<DepartamentoPage> {
    return this.http.get<DepartamentoPage>(`${this.api.baseUrl}/departamentos?page=${page}&size=${size}`);
  }

  getByClave(clave: string): Observable<Departamento> {
    return this.http.get<Departamento>(`${this.api.baseUrl}/departamentos/${clave}`);
  }

  create(payload: DepartamentoRequest): Observable<Departamento> {
    return this.http.post<Departamento>(`${this.api.baseUrl}/departamentos`, payload);
  }

  update(clave: string, payload: DepartamentoRequest): Observable<Departamento> {
    return this.http.put<Departamento>(`${this.api.baseUrl}/departamentos/${clave}`, payload);
  }

  remove(clave: string): Observable<void> {
    return this.http.delete<void>(`${this.api.baseUrl}/departamentos/${clave}`);
  }
}
