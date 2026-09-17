import { Component, input } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ResumoFinanceiro } from '../../models/lancamento.model';

@Component({
  selector: 'app-lancamento-resumo',
  standalone: true,
  imports: [CurrencyPipe, CommonModule],
  templateUrl: './lancamento-resumo.component.html',
  styleUrl: './lancamento-resumo.component.scss',
})
export class LancamentoResumoComponent {
  resumo = input.required<ResumoFinanceiro>();
}
