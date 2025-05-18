import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FilterBarComponent } from './filter-bar.component';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    FilterBarComponent
  ],
  exports: [
    FilterBarComponent
  ],
  imports: [
    CommonModule,
    MatIconModule,
    RouterModule,
    FormsModule
  ]
})
export class FilterBarModule { }
