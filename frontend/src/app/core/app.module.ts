import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AuthenticationService } from '../services/authentication/authentication.service';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthenticationGuard } from './guards/authentication-guard/authentication.guard';
import { TokenJwtInterceptor } from './interceptors/token-jwt/token-jwt.interceptor';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';
import { LoggedUserDetailsService } from '../services/logged-user-details/logged-user-details.service';
import { MAT_DIALOG_DEFAULT_OPTIONS } from '@angular/material/dialog';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FontAwesomeModule,
    HttpClientModule
  ],
  bootstrap: [
    AppComponent
  ],
  providers: [
    AuthenticationService,
    AuthenticationGuard,
    LoggedUserDetailsService,
    JwtHelperService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenJwtInterceptor,
      multi: true
    },
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    {
      provide: MAT_DIALOG_DEFAULT_OPTIONS,
      useValue: {
        height: '250px',
        width: '500px',
        disableClose: true,
      }
    }
  ]
})
export class AppModule { }
