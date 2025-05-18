import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { LoginFormComponent } from './login-form.component';
import { RouterModule } from '@angular/router';
import { LoadingModalModule } from 'src/app/shared/components/loading-modal/loading-modal.module';
import { MatDialogModule } from '@angular/material/dialog';

@NgModule({
  declarations: [
    LoginFormComponent
  ],
  exports: [
    LoginFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    RouterModule,
    MatDialogModule,
    LoadingModalModule
  ]
})
export class LoginFormModule { }
