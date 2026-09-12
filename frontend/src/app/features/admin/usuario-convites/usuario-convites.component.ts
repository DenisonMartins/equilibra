import { Component, inject, OnInit, signal, viewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ConviteService } from './services/convite.service';
import { ConviteFilter, ConviteItem } from './models/convite.model';
import { ConviteFormComponent } from './componentes/convite-form/convite-form.component';
import { ConviteFiltroComponent } from './componentes/convite-filtro/convite-filtro.component';
import { ConviteTabelaComponent } from './componentes/convite-tabela/convite-tabela.component';

@Component({
  selector: 'app-usuario-convites',
  standalone: true,
  imports: [FormsModule, ConviteFormComponent, ConviteFiltroComponent, ConviteTabelaComponent],
  templateUrl: './usuario-convites.component.html',
  styleUrl: './usuario-convites.component.scss',
})
export class UsuarioConvitesComponent implements OnInit {
  private conviteService = inject(ConviteService);
  private formComponent = viewChild(ConviteFormComponent);

  convites = signal<ConviteItem[]>([]);
  carregando = signal<boolean>(false);
  pagina = signal<number>(0);
  tamanho = signal<number>(15);
  totalPaginas = signal<number>(0);
  filtroAtivo = signal<ConviteFilter>({});

  mensagemSucesso = signal<string | null>(null);
  mensagemErro = signal<string | null>(null);

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados(): void {
    this.carregando.set(true);
    this.mensagemErro.set(null);

    this.conviteService.listar(this.filtroAtivo(), this.pagina(), this.tamanho()).subscribe({
      next: (res) => {
        this.convites.set(res.content);
        this.totalPaginas.set(res.totalPages);
        this.carregando.set(false);
      },
      error: () => {
        this.mensagemErro.set('Erro ao carregar lista de convites.');
        this.carregando.set(false);
      },
    });
  }

  onEnviarConvite(email: string): void {
    const form = this.formComponent();
    if (form) form.enviando.set(true);

    this.mensagemSucesso.set(null);
    this.mensagemErro.set(null);

    this.conviteService.enviarConvite(email).subscribe({
      next: () => {
        if (form) form.enviando.set(false);
        this.mensagemSucesso.set(`Convite enviado com sucesso para ${email}!`);
        this.carregarDados();
      },
      error: (err) => {
        if (form) form.enviando.set(false);
        const msg = err.error?.message || 'Falha ao enviar convite.';
        this.mensagemErro.set(msg);
      },
    });
  }

  onAplicarFiltro(filtro: ConviteFilter): void {
    this.filtroAtivo.set(filtro);
    this.pagina.set(0);
    this.carregarDados();
  }

  onTrocarPagina(novaPagina: number): void {
    this.pagina.set(novaPagina);
    this.carregarDados();
  }
}
