import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { HomeComponent } from './home/home.component';

const dashboardRoutes: Routes = [
  {
    path: "",
    component: DashboardComponent,

    children: [
      {
        path: "",
        pathMatch: "full",
        redirectTo: "home"
      },
      {
        path: "home",
        component: HomeComponent
      },

      {
        path: "chamados",
        loadChildren: () => import("./tickets/tickets.module").then(m => m.TicketsModule)
      },

      {
        path: "tecnicos",
        loadChildren: () => import("./technical/technical.module").then(m => m.TechnicalModule)
      },

      {
        path: "clientes",
        loadChildren: () => import("./customer/customer.module").then((m) => m.CustomerModule)
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(dashboardRoutes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }
