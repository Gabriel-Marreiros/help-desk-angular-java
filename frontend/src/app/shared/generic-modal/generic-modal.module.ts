import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GenericModalComponent } from './generic-modal.component';
import { MatDialogModule } from '@angular/material/dialog';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    GenericModalComponent
  ],
  exports: [
    GenericModalComponent
  ],
  imports: [
    CommonModule,
    MatDialogModule,
    RouterModule
  ]
})
export class GenericModalModule { }
