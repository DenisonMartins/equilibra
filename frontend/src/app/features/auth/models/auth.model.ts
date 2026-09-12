export type PerfilUsuario = 'ROLE_ADMIN' | 'ROLE_USER';

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  nome: string;
  email: string;
  perfil: PerfilUsuario;
}

export interface ValidarConviteResponse {
  valido: boolean;
  email: string | null;
}

export interface AceiteConviteRequest {
  hash: string;
  nome: string;
  senha: string;
}
