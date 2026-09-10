import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Auth } from '../../../core/services/auth';

@Component({
  selector: 'app-convite-aceito',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './convite-aceito.html',
  styleUrl: './convite-aceito.scss',
})
export class ConviteAceito implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(Auth);

  token = signal('');
  tokenValido = signal(false);
  emailConvidado = signal('');

  nome = signal('');
  senha = signal('');
  confirmaSenha = signal('');
  erroValidacao = signal<string | null>(null);

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const tokenParam = params['token'] ? String(params['token']).trim() : '';
      this.token.set(tokenParam);

      const validacao = this.authService.validarTokenConviteMock(tokenParam);
      this.tokenValido.set(validacao.valido);
      this.emailConvidado.set(validacao.email || '');
    });
  }

  handleCadastro(): void {
    this.erroValidacao.set(null);

    if (this.senha() !== this.confirmaSenha()) {
      this.erroValidacao.set('As senhas não conferem');
      return;
    }

    this.authService.aceitarConviteMock({
      token: this.token(),
      nome: this.nome(),
      senha: this.senha(),
    });
  }

  irParaLogin(): void {
    this.router.navigate(['/login']);
  }
}
