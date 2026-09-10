import { computed, Injectable, signal } from '@angular/core';

export type TipoLancamento = 'RECEITA' | 'DESPESA';

export interface Lancamento {
  id: number;
  descricao: string;
  valor: number;
  tipo: TipoLancamento;
  categoria: string;
  data: string;
}

@Injectable({
  providedIn: 'root',
})
export class Financeiro {
  private readonly _lancamentos = signal<Lancamento[]>([
    {
      id: 1,
      descricao: 'Salário Mensal',
      valor: 12500.0,
      tipo: 'RECEITA',
      categoria: 'Renda Principal',
      data: '2026-09-01',
    },
    {
      id: 2,
      descricao: 'Aluguel & Condomínio',
      valor: 3200.0,
      tipo: 'DESPESA',
      categoria: 'Moradia',
      data: '2026-09-02',
    },
    {
      id: 3,
      descricao: 'Supermercado Mensal',
      valor: 1450.8,
      tipo: 'DESPESA',
      categoria: 'Alimentação',
      data: '2026-09-03',
    },
    {
      id: 4,
      descricao: 'Consultoria Backend Extra',
      valor: 2800.0,
      tipo: 'RECEITA',
      categoria: 'Freelance',
      data: '2026-09-04',
    },
  ]);

  readonly lancamentos = this._lancamentos.asReadonly();

  readonly totalReceitas = computed(() =>
    this._lancamentos()
      .filter((l) => l.tipo === 'RECEITA')
      .reduce((acc, curr) => acc + curr.valor, 0),
  );

  readonly totalDespesas = computed(() =>
    this._lancamentos()
      .filter((l) => l.tipo === 'DESPESA')
      .reduce((acc, curr) => acc + curr.valor, 0),
  );

  readonly saldoAtual = computed(() => this.totalReceitas() - this.totalDespesas());

  readonly taxaPoupanca = computed(() => {
    const receitas = this.totalReceitas();
    if (receitas <= 0) {
      return 0;
    }
    const poupado = this.saldoAtual();
    return poupado > 0 ? (poupado / receitas) * 100 : 0;
  });

  adicionarLancamento(novo: Omit<Lancamento, 'id'>): void {
    const item: Lancamento = {
      ...novo,
      id: Number(Date.now()),
    };
    this._lancamentos.update((lista) => [item, ...lista]);
  }

  removerLancamento(id: number): void {
    this._lancamentos.update((lista) => lista.filter((l) => l.id !== id));
  }

  exportarExcelMock(): void {
    const cabecalho = 'Data,Descricao,Categoria,Tipo,Valor\n';
    const linhas = this._lancamentos()
      .map((l) => `${l.data},"${l.descricao}","${l.categoria}",${l.tipo},${l.valor.toFixed(2)}`)
      .join('\n');

    const blob = new Blob([cabecalho + linhas], { type: 'text/csv;charset=utf-8;' });
    this.downloadArquivo(blob, `extrato_equilibra_${new Date().toISOString().slice(0, 10)}.csv`);
  }

  exportarPdfMock(): void {
    const relatorio = `
EQUILIBRA - EXTRATO FINANCEIRO CONSOLIDADO
Gerado em: ${new Date().toLocaleString('pt-BR')}

RESUMO DO MÊS:
Receitas Totais: R$ ${this.totalReceitas().toFixed(2)}
Despesas Totais: R$ ${this.totalDespesas().toFixed(2)}
Saldo Líquido:   R$ ${this.saldoAtual().toFixed(2)}
Taxa Poupança:   ${this.taxaPoupanca().toFixed(1)}%

LANÇAMENTOS DETALHADOS:
${this._lancamentos()
  .map(
    (l) =>
      `[${l.data}] ${l.tipo === 'RECEITA' ? '(+)' : '(-)'} ${l.descricao.padEnd(25, ' ')} | ${l.categoria.padEnd(15, ' ')} | R$ ${l.valor.toFixed(2)}`,
  )
  .join('\n')}
    `.trim();

    const blob = new Blob([relatorio], { type: 'text/plain;charset=utf-8;' });
    this.downloadArquivo(blob, `relatorio_equilibra_${new Date().toISOString().slice(0, 10)}.txt`);
  }

  private downloadArquivo(blob: Blob, nomeArquivo: string): void {
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = nomeArquivo;
    a.click();
    URL.revokeObjectURL(url);
  }
}
