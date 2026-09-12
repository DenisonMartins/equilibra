import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  AceiteConviteRequest,
  LoginRequest,
  LoginResponse,
  ValidarConviteResponse,
} from '../models/auth.model';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private router = inject(Router);
  private http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/autenticacao`;

  private readonly _usuarioLogado = signal<LoginResponse | null>(this.obterUsuarioSalvo());
  readonly usuarioLogado = this._usuarioLogado.asReadonly();
  readonly isAdmin = computed(() => this._usuarioLogado()?.perfil === 'ROLE_ADMIN');

  login(credenciais: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, credenciais).pipe(
      tap((response) => {
        localStorage.setItem('equilibra_token', response.token);
        localStorage.setItem('equilibra_usuario', JSON.stringify(response));
        this._usuarioLogado.set(response);
      }),
    );
  }

  logout(): void {
    localStorage.removeItem('equilibra_token');
    localStorage.removeItem('equilibra_usuario');
    this._usuarioLogado.set(null);

    this.router.navigate(['/login']).then(sucesso => {
      if (!sucesso) {
        console.error('Falha ao redirecionar para a página de login');
      }
    });
  }

  obterToken(): string | null {
    return localStorage.getItem('equilibra_token');
  }

  validarHashConvite(token: string): Observable<ValidarConviteResponse> {
    return this.http.get<ValidarConviteResponse>(`${this.baseUrl}/validar-hash`, {
      params: { hash: token },
    });
  }

  aceitarConvite(dados: AceiteConviteRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/aceitar-convite`, dados);
  }

  private obterUsuarioSalvo(): LoginResponse | null {
    const data = localStorage.getItem('equilibra_usuario');
    return data ? JSON.parse(data) : null;
  }

  ehAdmin(): boolean {
    return this._usuarioLogado()?.perfil === 'ROLE_ADMIN';
  }
}
