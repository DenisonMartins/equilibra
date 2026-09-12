import { Component, input, output } from '@angular/core';
import { ConviteItem } from '../../models/convite.model';
import { DatePipe } from '@angular/common';
import { PaginacaoComponent } from '../../../../../shared/components/paginacao/paginacao.component';

@Component({
  selector: 'app-convite-tabela',
  standalone: true,
  imports: [DatePipe, PaginacaoComponent],
  templateUrl: './convite-tabela.component.html',
  styleUrl: './convite-tabela.component.scss',
})
export class ConviteTabelaComponent {
  itens = input<ConviteItem[]>([]);
  carregando = input<boolean>(false);
  paginaAtual = input<number>(0);
  totalPaginas = input<number>(0);

  atualizar = output<void>();
  trocarPagina = output<number>();
}
