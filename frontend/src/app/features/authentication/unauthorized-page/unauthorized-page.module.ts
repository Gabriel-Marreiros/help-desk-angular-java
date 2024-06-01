import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UnauthorizedPageComponent } from './unauthorized-page.component';



@NgModule({
  declarations: [
    UnauthorizedPageComponent
  ],
  exports: [
    UnauthorizedPageComponent
  ],
  imports: [
    CommonModule
  ]
})
export class UnauthorizedPageModule { }
