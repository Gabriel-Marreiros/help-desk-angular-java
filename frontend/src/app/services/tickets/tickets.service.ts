import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TicketStatusEnum } from 'src/app/typings/enums/ticket-status-enum';
import { IAllTicketsStatusSummary } from 'src/app/typings/interfaces/all-tickets-status-summary';
import { IPage } from 'src/app/typings/interfaces/page';
import { TicketModel } from 'src/app/typings/models/ticket.model';
import { environment } from 'src/environments/environment';

@Injectable()
export class TicketsService {

  private readonly BASE_URL: string = `${environment.apiUrl}/tickets`;

  constructor(private http: HttpClient) { }

  getAllTickets(): Observable<HttpResponse<Array<TicketModel>>> {
    return this.http.get<Array<TicketModel>>(`${this.BASE_URL}`, {
      responseType: "json",
      observe: 'response'
    });
  }

  getTicketsByStatusPaginated(page: number, size: number, status: string): Observable<IPage<TicketModel>> {
    let url: string = `${this.BASE_URL}/paginated?page=${page}&size=${size}`;

    if(status != "todos"){
      url += `&status=${status}`;
    }

    return this.http.get<IPage<TicketModel>>(url);
  }


  getTicketById(id: string): Observable<HttpResponse<TicketModel>> {
    return this.http.get<TicketModel>(`${this.BASE_URL}/${id}`, {
      responseType: "json",
      observe: 'response'
    });
  }


  saveTicket(ticket: TicketModel): Observable<HttpResponse<ArrayBuffer>> {
    return this.http.post<ArrayBuffer>(this.BASE_URL, ticket, {
      responseType: "json",
      observe: 'response'
    });
  }

  updateStatus(ticketId: string, nextStatus: TicketStatusEnum): Observable<HttpResponse<string>> {
    const url: string = `${this.BASE_URL}/update-status/${ticketId}`

    return this.http.put(url, nextStatus, {
      observe: 'response',
      responseType: 'text'
    })
  }

  getAllTicketsStatusSummary(): Observable<HttpResponse<IAllTicketsStatusSummary>> {
    const url: string = `${this.BASE_URL}/all-tickets-summary-status`;

    return this.http.get<IAllTicketsStatusSummary>(url, {
      responseType: "json",
      observe: 'response'
    })
  }


  updateTicket(id: string, ticketUpdate: Partial<TicketModel>): Observable<TicketModel>{
    const url: string = `${this.BASE_URL}/${id}`;

    return this.http.put<TicketModel>(url, ticketUpdate);
  }

  getTicketsBySearchTermPaginated(page: number, size: number, searchTerm: string): Observable<IPage<TicketModel>>{
    const url: string = `${this.BASE_URL}/by-search-term/paginated?page=${page}&size=${size}&searchTerm=${searchTerm}`

    return this.http.get<IPage<TicketModel>>(url);
  }
}
