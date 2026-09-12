export interface ConviteItem {
  id: number;
  email: string;
  token: string;
  expira_em: string;
  utilizado: boolean;
  criado_em: string;
}

export interface ConviteFilter {
  email?: string;
  expira_em_inicial?: string;
  expira_em_final?: string;
  criado_em_inicial?: string;
  criado_em_final?: string;
  utilizado?: boolean;
}
