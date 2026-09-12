import { Component, inject, signal } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  email = signal('');
  senha = signal('');
  carregando = signal(false);
  mensagemErro = signal<string | null>(null);

  autenticar(): void {
    if (!this.email() || !this.senha()) {
      this.mensagemErro.set('Preencha seu e-mail e senha.');
      return;
    }

    this.carregando.set(true);
    this.mensagemErro.set(null);

    this.authService
      .login({
        email: this.email(),
        senha: this.senha(),
      })
      .subscribe({
        next: () => {
          this.carregando.set(false);
          this.router.navigate(['/app/dashboard']);
        },
        error: (err) => {
          this.carregando.set(false);
          if (err.status === 401 || err.status === 403) {
            this.mensagemErro.set('E-mail ou senha incorretos.');
          } else {
            this.mensagemErro.set(err.error?.message || 'Falha ao conectar com o servidor.');
          }
        },
      });
  }
}
