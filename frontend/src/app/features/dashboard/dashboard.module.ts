import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { HeaderModule } from '../../shared/components/header/header.module';
import { SidebarModule } from './sidebar/sidebar.module';
import { BreadcrumbModule } from 'xng-breadcrumb';


@NgModule({
  declarations: [
    DashboardComponent,
  ],
  exports: [
    DashboardComponent
  ],
  imports: [
    CommonModule,
    HeaderModule,
    SidebarModule,
    DashboardRoutingModule,
    BreadcrumbModule
  ]
})
export class DashboardModule { }
