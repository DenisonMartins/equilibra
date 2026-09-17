import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import {
  LancamentoFilter,
  LancamentoItem,
  LancamentoRequest,
  ResumoFinanceiro,
} from '../models/lancamento.model';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/pagination.model';

@Injectable({
  providedIn: 'root',
})
export class LancamentoService {
  private http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/lancamentos`;

  listar(filtro: LancamentoFilter, page = 0, size = 15): Observable<PageResponse<LancamentoItem>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'data,desc');

    if (filtro.descricao?.trim()) params = params.set('descricao', filtro.descricao.trim());
    if (filtro.tipo) params = params.set('tipo', filtro.tipo);
    if (filtro.categoriaId) params = params.set('categoriaId', filtro.categoriaId);
    if (filtro.dataInicio) params = params.set('dataInicio', filtro.dataInicio);
    if (filtro.dataFim) params = params.set('dataFim', filtro.dataFim);
    if (filtro.pago !== undefined && filtro.pago !== null)
      params = params.set('pago', filtro.pago.toString());

    return this.http.get<PageResponse<LancamentoItem>>(this.baseUrl, { params });
  }

  obterResumo(mes: number, ano: number): Observable<ResumoFinanceiro> {
    const params = new HttpParams().set('mes', mes.toString()).set('ano', ano.toString());
    return this.http.get<ResumoFinanceiro>(`${this.baseUrl}/resumo`, { params });
  }

  criar(dados: LancamentoRequest): Observable<LancamentoItem> {
    return this.http.post<LancamentoItem>(this.baseUrl, dados);
  }

  atualizar(id: string, dados: LancamentoRequest): Observable<LancamentoItem> {
    return this.http.put<LancamentoItem>(`${this.baseUrl}/${id}`, dados);
  }

  remover(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
