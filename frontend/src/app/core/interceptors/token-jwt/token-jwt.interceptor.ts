import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable()
export class TokenJwtInterceptor implements HttpInterceptor {

  private authorizationTokenName: string = environment.authorizationTokenName;

  constructor() {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const tokenJwt: string | null = localStorage.getItem(this.authorizationTokenName);

    if(!tokenJwt){
      return next.handle(request);
    }

    let modifiedRequest: HttpRequest<unknown> = request.clone({
      setHeaders: {
        "Authorization": tokenJwt
      }
    });

    return next.handle(modifiedRequest);
  }
}
