import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { TicketsFormModule } from './form/tickets-form.module';
import { TicketsGridModule } from './grid/tickets-grid.module';
import { TicketsRoutingModule } from './tickets-routing.module';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    TicketsRoutingModule,
    TicketsGridModule,
    TicketsFormModule,
  ]
})
export class TicketsModule { }
