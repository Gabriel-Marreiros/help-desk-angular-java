import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HeaderComponent } from './header.component';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { UserProfileButtonModule } from 'src/app/shared/components/user-profile-button/user-profile-button.module';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    HeaderComponent
  ],
  exports: [
    HeaderComponent
  ],
  imports: [
    CommonModule,
    MatIconModule,
    MatTooltipModule,
    UserProfileButtonModule,
    RouterModule
  ],
  providers: [
    AuthenticationService
  ]
})
export class HeaderModule { }
