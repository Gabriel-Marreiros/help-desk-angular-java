import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { CustomerFormModule } from './form/customer-form.module';
import { CustomerRoutingModule } from './customer-routing.module';
import { CustomersListModule } from './list/customers-list.module';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    CustomerRoutingModule,
    CustomersListModule,
    CustomerFormModule,
  ]
})
export class CustomerModule { }
