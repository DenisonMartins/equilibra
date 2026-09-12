import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-paginacao',
  standalone: true,
  imports: [],
  templateUrl: './paginacao.component.html',
  styleUrl: './paginacao.component.scss',
})
export class PaginacaoComponent {
  paginaAtual = input<number>(0);
  totalPaginas = input<number>(0);
  carregando = input<boolean>(false);

  mudarPagina = output<number>();
}
