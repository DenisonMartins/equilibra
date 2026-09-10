import { computed, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

export interface UsuarioSession {
  nome: string;
  email: string;
  permissao: 'ROLE_ADMIN' | 'ROLE_USER';
}
export interface TokenValidoResponse {
  valido: boolean;
  email: string | null;
}

export interface ConviteItem {
  id: number;
  email: string;
  token: string;
  expiraEm: Date;
  utilizado: boolean;
  criadoEm: Date;
}

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly _usuarioCorrente = signal<UsuarioSession | null>(this.getStoredUsuario());

  readonly usuarioCorrente = this._usuarioCorrente.asReadonly();
  readonly isAutenticado = computed(() => !!this._usuarioCorrente());
  readonly isAdmin = computed(() => this._usuarioCorrente()?.permissao === 'ROLE_ADMIN');

  constructor(private router: Router) {}

  loginMock(email: string, admin: boolean = false): void {
    const session: UsuarioSession = {
      nome: admin ? 'Administrador' : 'Denison Martins',
      email,
      permissao: admin ? 'ROLE_ADMIN' : 'ROLE_USER',
    };

    localStorage.setItem('session', JSON.stringify(session));
    this._usuarioCorrente.set(session);
    this.router.navigate(['/app/dashboard']);
  }

  logout(): void {
    localStorage.removeItem('session');
    this._usuarioCorrente.set(null);
    this.router.navigate(['/login']);
  }

  validarTokenConviteMock(token: string): TokenValidoResponse {
    if (token && token.length >= 8) {
      return { valido: true, email: 'novo.usuario@exemplo.com' };
    }
    return { valido: false, email: null };
  }

  aceitarConviteMock(dados: { token: string; nome: string; senha: string }): void {
    const session: UsuarioSession = {
      nome: dados.nome,
      email: 'novo.usuario@exemplo.com',
      permissao: 'ROLE_USER',
    };

    localStorage.setItem('session', JSON.stringify(session));
    this._usuarioCorrente.set(session);
    this.router.navigate(['/app/dashboard']);
  }

  private getStoredUsuario(): UsuarioSession | null {
    const data = localStorage.getItem('session');
    return data ? JSON.parse(data) : null;
  }

  private readonly _convites = signal<ConviteItem[]>([
    {
      id: 1,
      email: 'novo.usuario@exemplo.com',
      token: 'abc123',
      expiraEm: new Date(Date.now() - 48 * 3600 * 1000),
      utilizado: false,
      criadoEm: new Date(),
    },
    {
      id: 2,
      email: 'novo.usuario2@exemplo.com',
      token: 'abc1234',
      expiraEm: new Date(Date.now() - 48 * 3600 * 1000),
      utilizado: false,
      criadoEm: new Date(Date.now() - 72 * 3600 * 1000),
    },
  ]);

  readonly convites = this._convites.asReadonly();

  enviarConviteMock(email: string): string {
    const token = 'tok_' + Math.random().toString(36).substring(2, 12) + Date.now().toString(36);
    const novo: ConviteItem = {
      id: Number(Date.now()),
      email,
      token,
      expiraEm: new Date(Date.now() + 48 * 3600 * 1000),
      utilizado: false,
      criadoEm: new Date(),
    };
    this._convites.update((convites) => [novo, ...convites]);
    return token;
  }
}
