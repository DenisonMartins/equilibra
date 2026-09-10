import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Shell } from './features/layout/shell/shell';
import { Dashboard } from './features/dashboard/dashboard';
import { ConviteAceito } from './features/auth/convite-aceito/convite-aceito';
import { UsuarioConvites } from './features/admin/usuario-convites/usuario-convites';
import { AiAdvisor } from './features/ai-advisor/ai-advisor/ai-advisor';
import { Lancamentos } from './features/lancamentos/lancamentos';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'convite', component: ConviteAceito },
  {
    path: 'app',
    component: Shell,
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'lancamentos', component: Lancamentos },
      { path: 'admin/usuarios', component: UsuarioConvites },
      { path: 'consultor-ia', component: AiAdvisor },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ],
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
];
