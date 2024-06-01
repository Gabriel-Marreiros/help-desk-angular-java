import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomersListComponent } from './list/customers-list.component';
import { CustomerFormComponent } from './form/customer-form.component';
import { AdminGuard } from 'src/app/core/guards/admin-guard/admin.guard';

const customerRoutes: Routes = [
  {
    path: "",
    pathMatch: "full",
    component: CustomersListComponent
  },
  {
    path: "formulario",
    component: CustomerFormComponent,
    canActivate: [AdminGuard]
  },
  {
    path: "detalhes/:id",
    component: CustomerFormComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(customerRoutes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
