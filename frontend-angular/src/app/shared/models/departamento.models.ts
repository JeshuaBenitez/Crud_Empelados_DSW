import { PageResponse } from './api.models';

export interface Departamento {
  clave: string;
  nombre: string;
}

export interface DepartamentoRequest {
  nombre: string;
}

export type DepartamentoPage = PageResponse<Departamento>;
