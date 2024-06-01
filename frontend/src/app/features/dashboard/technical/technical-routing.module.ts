import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TechnicalsListComponent } from './list/technicals-list.component';
import { TechnicalFormComponent } from './form/technical-form.component';
import { AdminGuard } from 'src/app/core/guards/admin-guard/admin.guard';

const technicalRoutes: Routes = [
  {
    path: "",
    pathMatch: "full",
    component: TechnicalsListComponent
  },
  {
    path: "formulario",
    component: TechnicalFormComponent,
    canActivate: [AdminGuard]
  },
  {
    path: "detalhes/:id",
    component: TechnicalFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(technicalRoutes)],
  exports: [RouterModule]
})
export class TechnicalRoutingModule { }
