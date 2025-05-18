import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import {MatDividerModule} from '@angular/material/divider';
import { RouterModule } from '@angular/router';
import { TicketsService } from 'src/app/services/tickets/tickets.service';
import { UserProfileButtonModule } from 'src/app/shared/components/user-profile-button/user-profile-button.module';
import { TicketsGridComponent } from './tickets-grid.component';
import { MatDialogModule } from '@angular/material/dialog';
import { LoadingModalModule } from 'src/app/shared/components/loading-modal/loading-modal.module';
import { FormsModule } from '@angular/forms';
import { ExcelService } from 'src/app/services/excel/excel.service';
import { MatSelectModule } from '@angular/material/select';
import { FilterBarModule } from 'src/app/shared/components/filter-bar/filter-bar.module';


@NgModule({
  declarations: [
    TicketsGridComponent
  ],
  exports: [
    TicketsGridComponent
  ],
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatDividerModule,
    MatSelectModule,
    RouterModule,
    UserProfileButtonModule,
    MatDialogModule,
    LoadingModalModule,
    FormsModule,
    FilterBarModule
  ],
  providers: [
    TicketsService,
    ExcelService
  ]
})
export class TicketsGridModule { }
