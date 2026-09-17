export type TipoLancamento = 'RECEITA' | 'DESPESA';

export interface CategoriaItem {
  id: string;
  nome: string;
  cor?: string;
  icone?: string;
}

export interface LancamentoItem {
  id: string;
  descricao: string;
  valor: number;
  data: string; // Formato YYYY-MM-DD
  tipo: TipoLancamento;
  categoria: CategoriaItem;
  pago: boolean;
}

export interface LancamentoRequest {
  descricao: string;
  valor: number;
  data: string;
  tipo: TipoLancamento;
  categoriaId: string;
  pago: boolean;
}

export interface LancamentoFilter {
  descricao?: string;
  tipo?: TipoLancamento;
  categoriaId?: string;
  dataInicio?: string;
  dataFim?: string;
  pago?: boolean;
}

export interface ResumoFinanceiro {
  totalReceitas: number;
  totalDespesas: number;
  saldo: number;
}
