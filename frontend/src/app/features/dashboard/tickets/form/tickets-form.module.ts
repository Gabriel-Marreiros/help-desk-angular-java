import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { PriorityService } from 'src/app/services/priority/priority.service';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { TicketsService } from 'src/app/services/tickets/tickets.service';
import { GenericModalModule } from 'src/app/shared/generic-modal/generic-modal.module';
import { TicketsFormComponent } from './tickets-form.component';
import { MatDialogModule } from '@angular/material/dialog';
import { LoadingModalModule } from 'src/app/shared/loading-modal/loading-modal.module';



@NgModule({
  declarations: [
    TicketsFormComponent
  ],
  exports: [
    TicketsFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDialogModule,
    GenericModalModule,
    LoadingModalModule
  ],
  providers: [
    TicketsService,
    TechnicalService,
    CustomerService,
    PriorityService
  ]
})
export class TicketsFormModule { }
