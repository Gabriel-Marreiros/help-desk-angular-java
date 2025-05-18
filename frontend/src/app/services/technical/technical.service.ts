import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IPage } from 'src/app/typings/interfaces/page';
import { ITechnicalWithTicketStatusCount } from 'src/app/typings/interfaces/technical-with-ticket-status-count';
import { TechnicalModel } from 'src/app/typings/models/technical.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TechnicalService {
  private readonly BASE_URL = `${environment.apiUrl}/technicians`;

  constructor(private http: HttpClient) { }

  getAllTechnicals(): Observable<HttpResponse<Array<TechnicalModel>>> {
    return this.http.get<Array<TechnicalModel>>(this.BASE_URL, {
      observe: 'response',
      responseType: 'json'
    });
  }

  getTechnicalsPaginated(page: number, size: number): Observable<IPage<TechnicalModel>>{
    const url: string = `${this.BASE_URL}/paginated`;

    return this.http.get<IPage<TechnicalModel>>(url, {
      params: {
        page,
        size
      }
    });
  }

  getAllTechnicalWithTicketsStatusCount(): Observable<Array<TechnicalModel>> {
    const url: string = `${this.BASE_URL}/with-tickets-status-count`;

    return this.http.get<Array<TechnicalModel>>(url);
  }

  getTechniciansWithTicketsStatusCountPaginated(params: {page: number, size: number, status?: string, search?: string}): Observable<IPage<ITechnicalWithTicketStatusCount>> {
    const url: string = `${this.BASE_URL}/with-tickets-status-count/paginated`;

    return this.http.get<IPage<ITechnicalWithTicketStatusCount>>(url, {
      params
    });
  }

  getAllActiveTechnicals(): Observable<HttpResponse<Array<TechnicalModel>>> {
    const url = `${this.BASE_URL}`

    return this.http.get<Array<TechnicalModel>>(url, {
      observe: 'response',
      responseType: 'json'
    })
  }

  geTechnicalById(id: string): Observable<HttpResponse<TechnicalModel>> {
    return this.http.get<TechnicalModel>(`${this.BASE_URL}/${id}`, {
      observe: 'response',
      responseType: 'json'
    });
  }

  updateTechnical(updatedTechnical: Partial<TechnicalModel>): Observable<HttpResponse<TechnicalModel>> {
    const url = `${this.BASE_URL}/${updatedTechnical.id}`;

    return this.http.put<TechnicalModel>(url, updatedTechnical, {
      observe: 'response',
      responseType: 'json'
    })
  }

  changeTechnicalActiveStatus(id: string): Observable<void> {
    const url: string = `${this.BASE_URL}/${id}`;

    return this.http.delete<void>(url);
  }
}
