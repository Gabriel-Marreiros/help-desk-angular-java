import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { UserFormComponent } from './user-form.component';
import { LoggedUserService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { ReactiveFormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    UserFormComponent
  ],
  exports: [
    UserFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  providers: [
    LoggedUserService
  ]
})
export class UserFormModule { }
