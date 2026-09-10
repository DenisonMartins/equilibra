import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Financeiro, TipoLancamento } from '../../core/services/financeiro';
import { CurrencyPipe, DatePipe } from '@angular/common';

@Component({
  selector: 'app-lancamentos',
  standalone: true,
  imports: [FormsModule, DatePipe, CurrencyPipe],
  templateUrl: './lancamentos.html',
  styleUrl: './lancamentos.scss',
})
export class Lancamentos {
  financeiroService = inject(Financeiro);

  filtroTexto = signal('');
  filtroTipo = signal<'TODOS' | 'RECEITA' | 'DESPESA'>('TODOS');
  filtroCategoria = signal('TODAS');

  modalAberto = signal(false);
  formDescricao = signal('');
  formValor = signal<number | null>(null);
  formTipo = signal<TipoLancamento>('DESPESA');
  formCategoria = signal('Moradia');
  formData = signal(new Date().toISOString().slice(0, 10));

  lancamentosFiltrados = computed(() => {
    const texto = this.filtroTexto().toLowerCase().trim();
    const tipo = this.filtroTipo();
    const categoria = this.filtroCategoria();

    return this.financeiroService.lancamentos().filter((item) => {
      const bateTexto = !texto || item.descricao.toLowerCase().includes(texto);
      const bateTipo = tipo === 'TODOS' || item.tipo === tipo;
      const bateCategoria = categoria === 'TODAS' || item.categoria === categoria;
      return bateTexto && bateTipo && bateCategoria;
    });
  });

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
