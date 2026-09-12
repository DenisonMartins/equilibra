import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ConviteFilter, ConviteItem } from '../models/convite.model';
import { PageResponse } from '../../../../core/models/pagination.model';
import { environment } from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ConviteService {
  private http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/admin/convites`;

  listar(filtro: ConviteFilter, page = 0, size = 15): Observable<PageResponse<ConviteItem>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'criadoEm,desc');

    return this.http.post<PageResponse<ConviteItem>>(
      `${this.baseUrl}/listagem`,
      filtro,
      { params }
    );
  }

  enviarConvite(email: string): Observable<ConviteItem> {
    return this.http.post<ConviteItem>(this.baseUrl, { email });
  }
}
