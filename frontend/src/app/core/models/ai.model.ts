export interface RecomendacaoIA {
  titulo: string;
  categoria: 'ATENCAO' | 'OPORTUNIDADE' | 'SUCESSO';
  descricao: string;
  impactoEstimado: string;
}

export interface DiagnosticoFinanceiro {
  scoreSaude: number;
  classificacao: string;
  resumoGeral: string;
  mesesReservaEmergencia: number;
  recomendacoes: RecomendacaoIA[];
}
