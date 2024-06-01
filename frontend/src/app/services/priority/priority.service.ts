import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PriorityModel } from 'src/app/typings/models/priority.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PriorityService {

  private readonly BASE_URL: string = `${environment.apiUrl}/priorities`;

  constructor(
    private http: HttpClient
  ) {}

  public getAllPriorities(): Observable<HttpResponse<Array<PriorityModel>>>{
    return this.http.get<Array<PriorityModel>>(this.BASE_URL, {
      observe: "response",
      responseType: "json"
    })
  }
}
