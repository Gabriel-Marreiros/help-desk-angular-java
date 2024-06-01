import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable, destroyPlatform } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { ILoginRequest } from 'src/app/typings/interfaces/login-request';
import { ILoginResponse } from 'src/app/typings/interfaces/login-response';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private readonly BASE_URL: string = `${environment.apiUrl}/auth`;
  private authorizationTokenName: string = environment.authorizationTokenName;

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  login(login: ILoginRequest): Observable<HttpResponse<ILoginResponse>>{
    const url: string = `${this.BASE_URL}/login`;

    return this.http.post<ILoginResponse>(url, login, {
      observe: "response",
      responseType: "json"
    })
  }

  logout(): void {
    localStorage.removeItem(this.authorizationTokenName);
    this.router.navigate(["login"]);
  }

  validateToken(token: string): Observable<boolean>{
    const url: string = `${this.BASE_URL}/validate-token`;

    return this.http.post<boolean>(url, token);
  }
}
