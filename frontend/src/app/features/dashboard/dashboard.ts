import { Component, inject, signal } from '@angular/core';
import { Financeiro, TipoLancamento } from '../../core/services/financeiro';
import { FormsModule } from '@angular/forms';
import { CurrencyPipe, DatePipe, DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [FormsModule, CurrencyPipe, DatePipe, DecimalPipe],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  financeiroService = inject(Financeiro);

  modalAberto = signal(false);

  formDescricao = signal('');
  formValor = signal<number | null>(null);
  formTipo = signal<TipoLancamento>('DESPESA');
  formCategoria = signal('Moradia');
  formData = signal(new Date().toISOString().slice(0, 10));

  salvarLancamento(): void {
    if (!this.formDescricao() || !this.formValor() || !this.formData()) {
      return;
    }

    this.financeiroService.adicionarLancamento({
      descricao: this.formDescricao(),
      valor: Number(this.formValor()),
      tipo: this.formTipo(),
      categoria: this.formCategoria(),
      data: this.formData(),
    });

    this.formDescricao.set('');
    this.formValor.set(null);
    this.modalAberto.set(false);
  }
}
