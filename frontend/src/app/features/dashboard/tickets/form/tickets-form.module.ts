import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { AngularEditorModule } from '@kolkov/angular-editor';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { PriorityService } from 'src/app/services/priority/priority.service';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { TicketCommentsService } from 'src/app/services/ticket-comments/ticket-comments.service';
import { TicketsService } from 'src/app/services/tickets/tickets.service';
import { GenericModalModule } from 'src/app/shared/components/generic-modal/generic-modal.module';
import { LoadingModalModule } from 'src/app/shared/components/loading-modal/loading-modal.module';
import { UserProfileButtonModule } from 'src/app/shared/components/user-profile-button/user-profile-button.module';
import { TicketsFormComponent } from './tickets-form.component';
import { CommentFormComponent } from './comment-form/comment-form.component';



@NgModule({
  declarations: [
    TicketsFormComponent,
    CommentFormComponent
  ],
  exports: [
    TicketsFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDialogModule,
    GenericModalModule,
    LoadingModalModule,
    AngularEditorModule,
    MatDividerModule,
    UserProfileButtonModule,
    MatIconModule,
  ],
  providers: [
    TicketsService,
    TechnicalService,
    CustomerService,
    PriorityService,
    TicketCommentsService
  ]
})
export class TicketsFormModule { }
