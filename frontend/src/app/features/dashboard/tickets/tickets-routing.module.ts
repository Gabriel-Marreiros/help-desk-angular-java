import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TicketsGridComponent } from './grid/tickets-grid.component';
import { TicketsFormComponent } from './form/tickets-form.component';

const ticketsRoutes: Routes = [
  {
    path: "",
    pathMatch: "full",
    component: TicketsGridComponent
  },
  {
    path: "formulario",
    component: TicketsFormComponent
  },
  {
    path: "formulario/:id",
    component: TicketsFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(ticketsRoutes)],
  exports: [RouterModule]
})
export class TicketsRoutingModule { }
