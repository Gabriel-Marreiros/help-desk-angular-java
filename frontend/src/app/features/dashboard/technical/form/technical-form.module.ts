import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { RouterModule } from '@angular/router';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { GenericModalModule } from 'src/app/shared/generic-modal/generic-modal.module';
import { LoadingModalModule } from 'src/app/shared/loading-modal/loading-modal.module';
import { TechnicalFormComponent } from './technical-form.component';


@NgModule({
  declarations: [
    TechnicalFormComponent
  ],
  exports: [
    TechnicalFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatDialogModule,
    GenericModalModule,
    LoadingModalModule,
    MatIconModule,
    RouterModule,
  ],
  providers: [
    TechnicalService,
    {provide: MAT_DATE_LOCALE, useValue: 'br'},
  ]
})
export class TechnicalFormModule { }
