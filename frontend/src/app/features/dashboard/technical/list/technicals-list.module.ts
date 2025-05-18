import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterModule } from '@angular/router';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { TechnicalsListComponent } from './technicals-list.component';
import { FilterBarModule } from 'src/app/shared/components/filter-bar/filter-bar.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';



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
    MatProgressSpinnerModule,
    FilterBarModule,
    MatFormFieldModule,
    MatSelectModule,
    FormsModule
  ],
  providers: [
    TechnicalService
  ]
})
export class TechnicalsListModule { }
