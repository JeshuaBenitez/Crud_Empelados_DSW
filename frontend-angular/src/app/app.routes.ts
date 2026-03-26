import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { guestGuard } from './core/auth/guest.guard';
import { LoginComponent } from './features/login/login.component';
import { AuthLayoutComponent } from './core/layout/auth-layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { EmpleadosListComponent } from './features/empleados/empleados-list.component';
import { EmpleadoFormComponent } from './features/empleados/empleado-form.component';
import { DepartamentosListComponent } from './features/departamentos/departamentos-list.component';
import { DepartamentoFormComponent } from './features/departamentos/departamento-form.component';

export const routes: Routes = [
	{
		path: 'login',
		component: LoginComponent,
		canActivate: [guestGuard]
	},
	{
		path: '',
		component: AuthLayoutComponent,
		canActivate: [authGuard],
		children: [
			{ path: 'dashboard', component: DashboardComponent },
			{ path: 'empleados', component: EmpleadosListComponent },
			{ path: 'empleados/nuevo', component: EmpleadoFormComponent },
			{ path: 'empleados/:clave/editar', component: EmpleadoFormComponent },
			{ path: 'departamentos', component: DepartamentosListComponent },
			{ path: 'departamentos/nuevo', component: DepartamentoFormComponent },
			{ path: 'departamentos/:clave/editar', component: DepartamentoFormComponent },
			{ path: '', pathMatch: 'full', redirectTo: 'dashboard' }
		]
	},
	{ path: '**', redirectTo: '' }
];
