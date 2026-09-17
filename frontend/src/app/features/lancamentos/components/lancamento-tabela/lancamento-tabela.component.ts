import { Component, input, output } from '@angular/core';
import { PaginacaoComponent } from '../../../../shared/components/paginacao/paginacao.component';
import { LancamentoItem } from '../../models/lancamento.model';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';

@Component({
  selector: 'app-lancamento-tabela',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, DatePipe, PaginacaoComponent],
  templateUrl: './lancamento-tabela.component.html',
  styleUrl: './lancamento-tabela.component.scss',
})
export class LancamentoTabelaComponent {
  itens = input<LancamentoItem[]>([]);
  carregando = input<boolean>(false);
  paginaAtual = input<number>(0);
  totalPaginas = input<number>(0);

  atualizar = output<void>();
  trocarPagina = output<number>();
  editar = output<LancamentoItem>();
  excluir = output<string>();
}
