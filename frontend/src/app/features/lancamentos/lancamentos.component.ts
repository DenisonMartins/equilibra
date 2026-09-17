import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  CategoriaItem,
  LancamentoFilter,
  LancamentoItem,
  LancamentoRequest,
  ResumoFinanceiro,
} from './models/lancamento.model';
import { LancamentoService } from './services/lancamento.service';
import { LancamentoResumoComponent } from './components/lancamento-resumo/lancamento-resumo.component';
import { LancamentoTabelaComponent } from './components/lancamento-tabela/lancamento-tabela.component';
import { LancamentoFiltroComponent } from './components/lancamento-filtro/lancamento-filtro.component';
import { CommonModule } from '@angular/common';
import { LancamentoFormModalComponent } from './components/lancamento-form-modal/lancamento-form-modal.component';

@Component({
  selector: 'app-lancamentos',
  standalone: true,
  imports: [
    CommonModule,
    LancamentoResumoComponent,
    LancamentoTabelaComponent,
    LancamentoFiltroComponent,
    LancamentoFormModalComponent,
  ],
  templateUrl: './lancamentos.component.html',
  styleUrl: './lancamentos.component.scss',
})
export class LancamentosComponent {
  private lancamentoService = inject(LancamentoService);

  lancamentos = signal<LancamentoItem[]>([]);
  resumo = signal<ResumoFinanceiro>({ totalReceitas: 0, totalDespesas: 0, saldo: 0 });
  carregando = signal<boolean>(false);
  pagina = signal<number>(0);
  tamanho = signal<number>(15);
  totalPaginas = signal<number>(0);
  filtroAtivo = signal<LancamentoFilter>({});
  mensagemFeedback = signal<string | null>(null);
  modalAberto = signal<boolean>(false);
  itemEmEdicao = signal<LancamentoItem | null>(null);
  categorias = signal<CategoriaItem[]>([
    { id: '1', nome: 'Alimentação', cor: '#ef4444' },
    { id: '2', nome: 'Salário', cor: '#10b981' },
    { id: '3', nome: 'Moradia', cor: '#3b82f6' },
  ]);

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados(): void {
    this.carregando.set(true);

    const hoje = new Date();
    this.lancamentoService.obterResumo(hoje.getMonth() + 1, hoje.getFullYear()).subscribe({
      next: (res) => this.resumo.set(res),
      error: () => {},
    });

    this.lancamentoService.listar(this.filtroAtivo(), this.pagina(), this.tamanho()).subscribe({
      next: (res) => {
        this.lancamentos.set(res.content);
        this.totalPaginas.set(res.totalPages);
        this.carregando.set(false);
      },
      error: () => {
        this.carregando.set(false);
      },
    });
  }

  onAplicarFiltro(filtro: LancamentoFilter): void {
    this.filtroAtivo.set(filtro);
    this.pagina.set(0);
    this.carregarDados();
  }

  onTrocarPagina(novaPagina: number): void {
    this.pagina.set(novaPagina);
    this.carregarDados();
  }

  abrirModalNovo(): void {
    this.itemEmEdicao.set(null);
    this.modalAberto.set(true);
  }

  onEditar(item: LancamentoItem): void {
    this.itemEmEdicao.set(item);
    this.modalAberto.set(true);
  }

  fecharModal(): void {
    this.modalAberto.set(false);
    this.itemEmEdicao.set(null);
  }

  onSalvarLancamento(payload: LancamentoRequest): void {
    const itemAtual = this.itemEmEdicao();

    if (itemAtual) {
      this.lancamentoService.atualizar(itemAtual.id, payload).subscribe({
        next: () => {
          this.mensagemFeedback.set('Lançamento atualizado com sucesso.');
          this.fecharModal();
          this.carregarDados();
        },
      });
    } else {
      this.lancamentoService.criar(payload).subscribe({
        next: () => {
          this.mensagemFeedback.set('Lançamento registrado com sucesso.');
          this.fecharModal();
          this.carregarDados();
        },
      });
    }
  }

  onExcluir(id: string): void {
    if (confirm('Deseja realmente excluir este lançamento?')) {
      this.lancamentoService.remover(id).subscribe({
        next: () => {
          this.mensagemFeedback.set('Lançamento removido com sucesso.');
          this.carregarDados();
        },
      });
    }
  }
}
