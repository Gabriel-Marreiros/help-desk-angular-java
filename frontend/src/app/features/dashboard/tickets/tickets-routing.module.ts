import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TicketsGridComponent } from './grid/tickets-grid.component';
import { TicketsFormComponent } from './form/tickets-form.component';
import { NewTicketGuard } from 'src/app/core/guards/new-ticket-guard/new-ticket.guard';

const ticketsRoutes: Routes = [
  {
    path: "",
    pathMatch: "full",
    component: TicketsGridComponent
  },
  {
    path: "formulario",
    component: TicketsFormComponent,
    canActivate: [NewTicketGuard],
    data: {
      breadcrumb: "Formulário"
    }
  },
  {
    path: "detalhes/:id",
    component: TicketsFormComponent,
    data: {
      breadcrumb: "Detalhes"
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(ticketsRoutes)],
  exports: [RouterModule]
})
export class TicketsRoutingModule { }
