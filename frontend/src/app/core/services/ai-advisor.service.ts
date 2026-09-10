import { inject, Injectable, signal } from '@angular/core';
import { Financeiro } from './financeiro';

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

@Injectable({
  providedIn: 'root',
})
export class AiAdvisorService {
  private financeiroService = inject(Financeiro);

  private readonly _carregando = signal(false);
  private readonly _diagnostico = signal<DiagnosticoFinanceiro | null>(null);

  readonly carregando = this._carregando.asReadonly();
  readonly diagnostico = this._diagnostico.asReadonly();

  gerarDiagnostico(): void {
    this._carregando.set(true);

    setTimeout(() => {
      const receitas = this.financeiroService.totalReceitas();
      const despesas = this.financeiroService.totalDespesas();
      const saldo = this.financeiroService.saldoAtual();
      const taxa = this.financeiroService.taxaPoupanca();

      const mesesReserva = despesas > 0 ? Number((saldo / despesas).toFixed(1)) : 0;
      let score = Math.min(100, Math.max(20, Math.round(taxa * 2.2 + 20)));

      this._diagnostico.set({
        scoreSaude: score,
        classificacao: score >= 75 ? 'Excelente' : score >= 50 ? 'Estável' : 'Requer Atenção',
        resumoGeral: `Com base nos seus R$ ${receitas.toFixed(2)} em receitas e taxa de poupança de ${taxa.toFixed(1)}%, seu fluxo de caixa está positivo. Há espaço para acelerar a formação da reserva de emergência e otimizar gastos em serviços e moradia.`,
        mesesReservaEmergencia: mesesReserva,
        recomendacoes: [
          {
            titulo: 'Consolidação de Reserva de Emergência',
            categoria: 'OPORTUNIDADE',
            descricao:
              'Seu saldo acumulado atual cobriria aproximadamente ' +
              mesesReserva +
              ' meses de custo fixo. O recomendado é manter de 6 a 12 meses em CDB com liquidez diária ou Tesouro Selic.',
            impactoEstimado: '+ Segurança Patrimonial',
          },
          {
            titulo: 'Otimização de Custos de Moradia',
            categoria: 'ATENCAO',
            descricao:
              'Os gastos com moradia e condomínio representam parcela expressiva das despesas. Avalie renegociação de planos de internet ou redução de contas de consumo.',
            impactoEstimado: 'Economia estimada: R$ 250,00 / mês',
          },
          {
            titulo: 'Aporte Programado pós-Recebimento',
            categoria: 'SUCESSO',
            descricao:
              'Sua taxa de retenção está saudável. A melhor estratégia para este mês é automatizar o investimento de 20% da renda logo no primeiro dia útil.',
            impactoEstimado: 'Crescimento de juros compostos a médio prazo',
          },
        ],
      });

      this._carregando.set(false);
    }, 1200);
  }
}
