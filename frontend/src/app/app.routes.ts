import { Routes } from '@angular/router';
import { ShellComponent } from './features/layout/shell/shell.component';
import { Dashboard } from './features/dashboard/dashboard';
import { ConviteAceitoComponent } from './features/auth/components/convite-aceito/convite-aceito.component';
import { UsuarioConvitesComponent } from './features/admin/usuario-convites/usuario-convites.component';
import { AiAdvisor } from './features/ai-advisor/ai-advisor/ai-advisor';
import { Lancamentos } from './features/lancamentos/lancamentos';
import { adminGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/components/login/login.component').then((m) => m.LoginComponent),
  },
  { path: 'convite', component: ConviteAceitoComponent },
  {
    path: 'app',
    component: ShellComponent,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'lancamentos', component: Lancamentos },
      { path: 'consultor-ia', component: AiAdvisor },
      {
        path: 'admin/usuarios',
        component: UsuarioConvitesComponent,
        canActivate: [adminGuard],
      },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ],
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
];
