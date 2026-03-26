import { PageResponse } from './api.models';

export interface Empleado {
  clave: string;
  nombre: string;
  direccion: string;
  telefono: string;
  departamentoClave?: string;
  departamentoNombre?: string;
}

export interface EmpleadoRequest {
  nombre: string;
  direccion: string;
  telefono: string;
}

export type EmpleadoPage = PageResponse<Empleado>;
