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
        component: HomeComponent,
        data: {
          breadcrumb: "Início"
        }
      },

      {
        path: "chamados",
        loadChildren: () => import("./tickets/tickets.module").then(m => m.TicketsModule),
        data: {
          breadcrumb: "Chamados"
        }
      },

      {
        path: "tecnicos",
        loadChildren: () => import("./technical/technical.module").then(m => m.TechnicalModule),
        data: {
          breadcrumb: "Técnicos"
        }
      },

      {
        path: "clientes",
        loadChildren: () => import("./customer/customer.module").then((m) => m.CustomerModule),
        data: {
          breadcrumb: "Clientes"
        }
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(dashboardRoutes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule { }
