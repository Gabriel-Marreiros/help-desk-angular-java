import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { HeaderModule } from '../../shared/header/header.module';
import { SidebarModule } from './sidebar/sidebar.module';


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
  ]
})
export class DashboardModule { }
