import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterModule } from '@angular/router';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { CustomersListComponent } from './customers-list.component';



@NgModule({
  declarations: [
    CustomersListComponent
  ],
  exports: [
    CustomersListComponent
  ],
  imports: [
    CommonModule,
    MatCardModule,
    RouterModule,
    MatProgressSpinnerModule
  ],
  providers: [
    CustomerService
  ]
})
export class CustomersListModule { }
