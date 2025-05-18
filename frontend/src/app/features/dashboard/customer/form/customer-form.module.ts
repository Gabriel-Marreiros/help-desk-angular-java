import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerFormComponent } from './customer-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LoadingModalModule } from 'src/app/shared/components/loading-modal/loading-modal.module';
import { GenericModalModule } from 'src/app/shared/components/generic-modal/generic-modal.module';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { UserFormModule } from 'src/app/shared/components/user-form/user-form.module';



@NgModule({
  declarations: [
    CustomerFormComponent
  ],
  exports: [
    CustomerFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatIconModule,
    RouterModule,
    MatDialogModule,
    MatProgressSpinnerModule,
    LoadingModalModule,
    GenericModalModule,
    UserFormModule
  ],
  providers: [
    CustomerService,
    AuthenticationService
  ]
})
export class CustomerFormModule { }
