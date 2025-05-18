import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserProfileButtonComponent } from './user-profile-button.component';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    UserProfileButtonComponent
  ],
  exports: [
    UserProfileButtonComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ]
})
export class UserProfileButtonModule { }
