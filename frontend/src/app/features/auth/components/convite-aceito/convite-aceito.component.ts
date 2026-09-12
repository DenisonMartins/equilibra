import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-convite-aceito',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './convite-aceito.component.html',
  styleUrl: './convite-aceito.component.scss',
})
export class ConviteAceitoComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);

  hashConvite = signal('');
  emailConvidado = signal('');
  conviteValido = signal(false);
  carregandoValidacao = signal(true);
  salvando = signal(false);
  erroAtivacao = signal<string | null>(null);

  nome = signal('');
  senha = signal('');

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const hashParam = params['hash'];
      if (hashParam) {
        this.hashConvite.set(hashParam);
        this.validarHash(hashParam);
      } else {
        this.carregandoValidacao.set(false);
      }
    });
  }

  validarHash(token: string): void {
    this.authService.validarHashConvite(token).subscribe({
      next: (res) => {
        this.conviteValido.set(res.valido);
        if (res.email) {
          this.emailConvidado.set(res.email);
        }
        this.carregandoValidacao.set(false);
      },
      error: () => {
        this.conviteValido.set(false);
        this.carregandoValidacao.set(false);
      },
    });
  }

  concluirAtivacao(): void {
    if (!this.nome() || !this.senha() || this.senha().length < 8) {
      this.erroAtivacao.set('Preencha seu nome e forneça uma senha de pelo menos 8 dígitos.');
      return;
    }

    this.salvando.set(true);
    this.erroAtivacao.set(null);

    this.authService.aceitarConvite({
        hash: this.hashConvite(),
        nome: this.nome(),
        senha: this.senha(),
      })
      .subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (err) => {
          this.erroAtivacao.set(err.error?.message || 'Erro ao ativar conta.');
          this.salvando.set(false);
        },
      });
  }
}
