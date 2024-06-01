import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterModule } from '@angular/router';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { TechnicalsListComponent } from './technicals-list.component';



@NgModule({
  declarations: [
    TechnicalsListComponent
  ],
  exports: [
    TechnicalsListComponent
  ],
  imports: [
    CommonModule,
    MatCardModule,
    RouterModule,
    MatProgressSpinnerModule
  ],
  providers: [
    TechnicalService
  ]
})
export class TechnicalsListModule { }
