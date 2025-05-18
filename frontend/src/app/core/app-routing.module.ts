import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticationGuard } from './guards/authentication-guard/authentication.guard';
import { PageNotFoundComponent } from '../features/page-not-found/page-not-found.component';

const routes: Routes = [
  {
    path: "",
    pathMatch: 'prefix',
    loadChildren: () => import("../features/authentication/authentication.module").then((m) => m.AuthenticationModule)
  },
  {
    path: 'dashboard',
    loadChildren: () => import('../features/dashboard/dashboard.module').then((m) => m.DashboardModule),
    canActivate: [AuthenticationGuard],
    data: {
      breadcrumb: "Dashboard"
    }
  },
  {
    path: "**",
    pathMatch: 'full',
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
