import { NgModule } from '@angular/core';

import { TechnicalFormModule } from './form/technical-form.module';
import { TechnicalsListModule } from './list/technicals-list.module';
import { TechnicalRoutingModule } from './technical-routing.module';


@NgModule({
  declarations: [],
  imports: [
    TechnicalRoutingModule,
    TechnicalFormModule,
    TechnicalsListModule
  ]
})
export class TechnicalModule { }
