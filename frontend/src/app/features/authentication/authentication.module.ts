import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { AuthenticationRoutingModule } from './authentication-routing.module';
import { LoginFormModule } from './login-form/login-form.module';
import { UnauthorizedPageModule } from './unauthorized-page/unauthorized-page.module';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    AuthenticationRoutingModule,
    LoginFormModule,
    UnauthorizedPageModule
  ]
})
export class AuthenticationModule { }
