import { Location } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { formatDistanceToNowStrict } from 'date-fns';
import ptBRLocale from 'date-fns/locale/pt-BR';
import { BehaviorSubject } from 'rxjs';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { LoggedUserService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { PriorityService } from 'src/app/services/priority/priority.service';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { TicketCommentsService } from 'src/app/services/ticket-comments/ticket-comments.service';
import { TicketsService } from 'src/app/services/tickets/tickets.service';
import { GenericModalComponent } from 'src/app/shared/components/generic-modal/generic-modal.component';
import { LoadingModalComponent } from 'src/app/shared/components/loading-modal/loading-modal.component';
import { TicketStatusEnum } from 'src/app/typings/enums/ticket-status-enum';
import { ILoggedUser } from 'src/app/typings/interfaces/logged-user-details';
import { CustomerModel } from 'src/app/typings/models/customer.model';
import { PriorityModel } from 'src/app/typings/models/priority.model';
import { TechnicalModel } from 'src/app/typings/models/technical.model';
import { TicketCommentModel } from 'src/app/typings/models/ticketComment.model';

@Component({
  selector: 'app-tickets-form',
  templateUrl: './tickets-form.component.html',
  styleUrls: ['./tickets-form.component.scss']
})
export class TicketsFormComponent implements OnInit {

  ticketForm: FormGroup = new FormGroup({
    id: new FormControl(),
    code: new FormControl(),
    title: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
    customer: new FormControl('', [Validators.required]),
    technical: new FormControl(''),
    openingDate: new FormControl(new Date().toISOString()),
    closedDate: new FormControl(),
    priority: new FormControl('', [Validators.required]),
    attachments: new FormControl(),
    status: new FormControl(TicketStatusEnum.NEW_TICKET)
  })

  technicalOptions!: Array<TechnicalModel>;
  customerOptions!: Array<CustomerModel>;
  priorityOptions!: Array<PriorityModel>;

  ticketStatusEnum = TicketStatusEnum;

  isNewTicket: boolean = true;
  isDetailsForm: boolean = false;
  formIsBeingEdited: boolean = false;

  ticketBackgroundStyleMap: Record<TicketStatusEnum, string> = {
    [TicketStatusEnum.NEW_TICKET]: "text-bg-secondary",
    [TicketStatusEnum.PENDING]: "text-bg-warning",
    [TicketStatusEnum.IN_PROGRESS]: "text-bg-primary",
    [TicketStatusEnum.RESOLVED]: "text-bg-success",
    [TicketStatusEnum.CANCELED]: "text-bg-danger"
  }

  commentForm: FormGroup = new FormGroup({
    id: new FormControl(),
    comment: new FormControl('', [Validators.required, Validators.maxLength(3000)]),
    user: new FormControl('', [Validators.required]),
    ticket: new FormControl('', [Validators.required]),
    edited: new FormControl(false, [Validators.required]),
    createdAt: new FormControl('', [Validators.required]),
    updatedAt: new FormControl()
  })

  commentPage: number = 0;
  commentPageSize: number = 5;
  finishComments: boolean = false;
  ticketComments$: BehaviorSubject<Array<TicketCommentModel>> = new BehaviorSubject<Array<TicketCommentModel>>([]);

  loggedUser!: ILoggedUser;

  addingNewComment: boolean = false;
  totalComments!: number;
  onEditing!: string | null;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private ticketsService: TicketsService,
    private technicalServices: TechnicalService,
    private customerServices: CustomerService,
    private priorityServices: PriorityService,
    private ticketCommentsServices: TicketCommentsService,
    private loggedUserService: LoggedUserService,
    public dialog: MatDialog
  ){}

  ngOnInit(): void {
    this.loggedUser = this.loggedUserService.getUserDetails();

    this.getPriorityOptions();
    this.getTechnicalOptions();
    this.getCustomerOptions();

    const ticketId = this.route.snapshot.params["id"];

    if(ticketId){
      this.loadTicketDetails(ticketId);
      this.loadTicketComments(ticketId);

      this.ticketForm.disable();
      this.isNewTicket = false;
      this.isDetailsForm = true;
    }

    if(this.isNewTicket && this.isCustomer()){
      const customerControl: AbstractControl = this.ticketForm.get("customer")!;
      customerControl.setValue(this.loggedUser.id);
      customerControl.disable();
    }
  }

  private getPriorityOptions(){
    this.priorityServices.getAllPriorities().subscribe({
      next: (response) => {
        this.priorityOptions = response.body!;
      }
    })
  }

  private getTechnicalOptions(): void {
    this.technicalServices.getAllActiveTechnicals().subscribe({
      next: (data) => {
        this.technicalOptions = data.body!
      }
    });
  }


  private getCustomerOptions(): void {
    this.customerServices.getAllCustomers().subscribe({
      next: (data) => {
        this.customerOptions = data.body!
      }
    });
  }

  loadTicketDetails(ticketId: string){
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.ticketsService.getTicketById(ticketId).subscribe({
      next: (data) => {
        this.ticketForm.patchValue({
          id: data.body?.id,
          code: data.body?.code,
          title: data.body?.title,
          description: data.body?.description,
          customer: data.body?.customer.id,
          technical: data.body?.technical?.id,
          openingDate: data.body?.openingDate,
          closedDate: data.body?.closedDate,
          priority: data.body?.priority.id,
          attachments: "",
          status: data.body?.ticketStatus
        });

        loadingModalRef.close();
      },

      error: (error) => {
        console.error(error);
        loadingModalRef.close();
      }
    })
  }


  saveTicket(): void {
    if(this.ticketForm.invalid) return;

    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.ticketForm.get("customer")?.enable();

    this.ticketsService.saveTicket(this.ticketForm.value).subscribe({
        next: (response) => {
          loadingModalRef.close();

          const genericModalRef = this.dialog.open(GenericModalComponent)
          genericModalRef.componentInstance.contentMessage = "Chamado aberto com sucesso!";
          genericModalRef.componentInstance.redirectLink = "dashboard/chamados";
        },

        error: (error: HttpErrorResponse) => {
          loadingModalRef.close();

          const genericModalRef = this.dialog.open(GenericModalComponent)
          genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao abrir o chamado! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!";
          genericModalRef.componentInstance.redirectLink = "dashboard/chamados";
        }
      }
    )
  }

  updateTicket(): void {
    if(this.ticketForm.invalid) return;

    const ticketId: string = this.ticketForm.get("id")?.value;

    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.ticketsService.updateTicket(ticketId, this.ticketForm.value).subscribe({
      next: (response) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Chamado atualizado com sucesso!";
        genericModalRef.componentInstance.redirectLink = "dashboard/chamados";
      },

      error: (error: HttpErrorResponse) => {
        console.error(error);

        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao atualizar o chamado! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!";
        genericModalRef.componentInstance.redirectLink = "dashboard/chamados";
      }
    })
  }

  assignTicketTechnical(): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    const ticketId = this.ticketForm.get("id")?.value;
    const technicalId = this.loggedUser.id;

    this.ticketsService.assignTicketTechnical(ticketId, {technicalId}).subscribe({
        next: (response) => {
          loadingModalRef.close();

          const genericModalRef = this.dialog.open(GenericModalComponent)
          genericModalRef.componentInstance.contentMessage = "Chamado atribuído com sucesso!";
          genericModalRef.componentInstance.redirectLink = "dashboard/chamados";
        },

        error: (error: HttpErrorResponse) => {
          loadingModalRef.close();

          const genericModalRef = this.dialog.open(GenericModalComponent)
          genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao atribuir o chamado! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!";
          genericModalRef.componentInstance.redirectLink = "dashboard/chamados";
        }
      }
    )
  }

  changeTicketStatus(nextStatus: TicketStatusEnum): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    const ticketId = this.ticketForm.get("id")?.value;

    this.ticketsService.updateTicketStatus(ticketId, nextStatus).subscribe({
      next: (response) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Status do chamado atualizado com sucesso!";
        genericModalRef.componentInstance.redirectLink = "dashboard/chamados";
      },

      error: (error: HttpErrorResponse) => {
        console.error(error);

        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao atualizar o status do chamado! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!";
        genericModalRef.componentInstance.redirectLink = "dashboard/chamados";
      }
    })
  }

  getTicketStatus(): TicketStatusEnum {
    return this.ticketForm.get('status')?.value;
  }

  isCustomer(): boolean {
    return this.loggedUserService.isCustomer();
  }

  isTechnical(): boolean {
    return this.loggedUserService.isTechnical();
  }

  isAdmin(): boolean {
    return this.loggedUserService.isAdmin();
  }

  isTicketCustomer(): boolean {
    return this.loggedUser.id === this.ticketForm.get("customer")?.value;
  }

  isTicketTechnical(): boolean {
    return this.loggedUser.id === this.ticketForm.get("technical")?.value;
  }

  ticketStatusIs(ticketStatus: TicketStatusEnum): boolean {
    return this.ticketForm.get("status")?.value === ticketStatus;
  }

  enableFormEditing(): void {
    this.ticketForm.get("title")?.enable();
    this.ticketForm.get("description")?.enable();
    this.ticketForm.get("priority")?.enable();

    if(this.isAdmin()){
      this.ticketForm.get("customer")?.enable();
      this.ticketForm.get("technical")?.enable();
    }

    this.formIsBeingEdited = true;
  }

  canEditTicket(): boolean {
    return (this.isDetailsForm &&
      (!this.ticketStatusIs(TicketStatusEnum.RESOLVED) && !this.ticketStatusIs(TicketStatusEnum.CANCELED)) &&
      (this.isTicketCustomer() || this.isAdmin()));
  }

  return(): void {
    this.location.back();
  }


  // ############## LÓGICA DE COMENTÁRIOS ##############
  formateCommentCreatedAt(date: Date): string {
    const createdAt = new Date(date);

    const distance = formatDistanceToNowStrict(createdAt, {
      locale: ptBRLocale
    })

    return `${distance} atrás`;
  }

  private loadTicketComments(ticketId: string): void {
    this.ticketCommentsServices.getAllTicketComments(ticketId, {page: this.commentPage, size: this.commentPageSize}).subscribe({
      next: (response) => {
        this.finishComments = response.body!.last;
        const comments = this.ticketComments$.value.concat(response.body?.content || []);
        this.ticketComments$.next(comments);

        this.totalComments = response.body?.totalElements || 0;
      }
    });
  }

  loadMoreComments(){
    if(this.finishComments){
      return;
    }

    this.commentPage++
    const ticketId: string = this.ticketForm.get('id')?.value;

    this.loadTicketComments(ticketId);
  }

  saveComment(): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.commentForm.patchValue({
      user: this.loggedUser.id,
      ticket: this.ticketForm.get("id")?.value,
      edited: false,
      createdAt: new Date(),
    });

    if(this.commentForm.invalid){
      loadingModalRef.close();
      return;
    }

    this.ticketCommentsServices.saveComment(this.commentForm.value).subscribe({
        next: (response) => {
          const savedComment = response.body!

          this.ticketComments$.value.unshift(savedComment);
          this.ticketComments$.next(this.ticketComments$.value);

          this.totalComments++;

          this.disableNewComment();

          loadingModalRef.close();
        },

        error: (error: HttpErrorResponse) => {
          loadingModalRef.close();

          const genericModalRef = this.dialog.open(GenericModalComponent)
          genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao adicionar o comentário! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!";
          genericModalRef.componentInstance.showCloseButton = true;
        }
      }
    )
  }

  updateComment(): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    if(this.commentForm.invalid){
      loadingModalRef.close();
      return;
    }

    const commentId: string = this.commentForm.get("id")?.value;

    this.ticketCommentsServices.updateComment(commentId, this.commentForm.value).subscribe({
        next: (response) => {
          const savedComment = response.body!
          const updatedTicketComentsList = this.ticketComments$.value.map((comment) => {
            if(comment.id == savedComment.id){
              return savedComment
            }

            return comment;
          });

          this.ticketComments$.next(updatedTicketComentsList);

          this.disableCommentUpdate();

          loadingModalRef.close();
        },

        error: (error: HttpErrorResponse) => {
          loadingModalRef.close();

          const genericModalRef = this.dialog.open(GenericModalComponent)
          genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao adicionar o comentário! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!";
          genericModalRef.componentInstance.showCloseButton = true;
        }
      }
    )
  }

  addNewComment(): void {
    this.disableCommentUpdate();
    this.addingNewComment = true;
  }

  disableNewComment(): void {
    this.addingNewComment = false;
    this.commentForm.reset();
  }

  enableCommentUpdate(comment: TicketCommentModel): void {
    this.disableNewComment();

    this.commentForm.patchValue({...comment, user: comment.user.id});
    this.onEditing = comment.id;
  }

  disableCommentUpdate(): void {
    this.commentForm.reset();
    this.onEditing = null;
  }

  canUpdateComment(userId: string): boolean {
    const loggedUserId: string = this.loggedUser.id;

    return userId === loggedUserId;
  }

  isEditing(id: string): boolean {
    return id == this.onEditing;
  }
}
