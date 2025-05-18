import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { AuthenticationRoutingModule } from './authentication-routing.module';
import { LoginFormModule } from './login-form/login-form.module';
import { UnauthorizedPageModule } from './unauthorized-page/unauthorized-page.module';
import { RegisterFormModule } from './register-form/register-form.module';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    AuthenticationRoutingModule,
    LoginFormModule,
    RegisterFormModule,
    UnauthorizedPageModule
  ]
})
export class AuthenticationModule { }
