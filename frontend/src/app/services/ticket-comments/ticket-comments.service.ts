import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IPage } from 'src/app/typings/interfaces/page';
import { TicketCommentModel } from 'src/app/typings/models/ticketComment.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TicketCommentsService {

  private readonly URL: string = `${environment.apiUrl}/ticket/comments`

  constructor(
    private http: HttpClient
  ) { }

  getAllTicketComments(ticketId: string, pagination: {page: number, size: number}): Observable<HttpResponse<IPage<TicketCommentModel>>>{
    const url: string  = `${this.URL}/${ticketId}`;

    const params = new HttpParams({fromObject: pagination});

    return this.http.get<IPage<TicketCommentModel>>(url, {
      params: params,
      responseType: "json",
      observe: 'response'
    });
  }

  saveComment(ticketComment: Partial<TicketCommentModel>): Observable<HttpResponse<TicketCommentModel>>{
    return this.http.post<TicketCommentModel>(this.URL, ticketComment, {
      responseType: "json",
      observe: 'response'
    })
  }

  updateComment(ticketCommentId: string, ticketCommentUpdate: Pick<TicketCommentModel, "comment">): Observable<HttpResponse<TicketCommentModel>>{
    const url: string = `${this.URL}/${ticketCommentId}`;

    return this.http.put<TicketCommentModel>(url, ticketCommentUpdate, {
      responseType: "json",
      observe: 'response'
    });
  }

  deleteComment(ticketCommentId: string): Observable<HttpResponse<void>>{
    const url: string = `${this.URL}/${ticketCommentId}`;

    return this.http.delete<void>(url, {
      responseType: "json",
      observe: 'response'
    });
  }

}
