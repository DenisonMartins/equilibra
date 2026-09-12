export type TipoLancamento = 'RECEITA' | 'DESPESA';

export interface Lancamento {
  id: number;
  descricao: string;
  valor: number;
  tipo: TipoLancamento;
  categoria: string;
  data: string;
}
