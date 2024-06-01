import { Location } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { LoggedUserDetailsService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { PriorityService } from 'src/app/services/priority/priority.service';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { TicketsService } from 'src/app/services/tickets/tickets.service';
import { GenericModalComponent } from 'src/app/shared/generic-modal/generic-modal.component';
import { LoadingModalComponent } from 'src/app/shared/loading-modal/loading-modal.component';
import { TicketStatusEnum } from 'src/app/typings/enums/ticket-status-enum';
import { CustomerModel } from 'src/app/typings/models/customer.model';
import { PriorityModel } from 'src/app/typings/models/priority.model';
import { TechnicalModel } from 'src/app/typings/models/technical.model';

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
    technical: new FormControl('', [Validators.required]),
    openingDate: new FormControl(new Date().toISOString()),
    closedDate: new FormControl(),
    priority: new FormControl('', [Validators.required]),
    attachments: new FormControl(),
    status: new FormControl()
  })

  technicalOptions!: Array<TechnicalModel>;
  customerOptions!: Array<CustomerModel>;
  priorityOptions!: Array<PriorityModel>;

  ticketStatusEnum = TicketStatusEnum;

  isDetailsForm: boolean = false;
  formIsBeingEdited: boolean = false;

  ticketBackgroundStyleMap: Record<TicketStatusEnum | 'Novo Chamado', string> = {
    [TicketStatusEnum.PENDING]: 'text-bg-primary',
    [TicketStatusEnum.IN_PROGRESS]: 'text-bg-warning',
    [TicketStatusEnum.RESOLVED]: 'text-bg-success',
    ['Novo Chamado']: 'text-bg-secondary'
  }

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private ticketsService: TicketsService,
    private technicalServices: TechnicalService,
    private customerServices: CustomerService,
    private priorityServices: PriorityService,
    private loggedUserDetailsService: LoggedUserDetailsService,
    public dialog: MatDialog
  ){}

  ngOnInit(): void {

    this.getTechnicalOptions();
    this.getCustomerOptions();
    this.getPriorityOptions();

    const ticketId = this.route.snapshot.paramMap.get('id');
    ticketId && this.loadTicketDetails(ticketId);
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
          customer: data.body?.customer.customerId,
          technical: data.body?.technical.technicalId,
          openingDate: data.body?.openingDate,
          closedDate: data.body?.closedDate,
          priority: data.body?.priority.id,
          attachments: "",
          status: data.body?.ticketStatus
        });

        this.ticketForm.disable();
        this.isDetailsForm = true;

        loadingModalRef.close();
      },

      error: (error) => {
        console.error(error);
        loadingModalRef.close();
      }
    })
  }


  saveTicket(): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.ticketsService.saveTicket(this.ticketForm.value).subscribe({
        next: (response) => {
          loadingModalRef.close();

          const genericModalRef = this.dialog.open(GenericModalComponent)
          genericModalRef.componentInstance.contentMessage = "Chamado aberto com sucesso!";
          genericModalRef.componentInstance.redirectLink = "dashboard/chamados";
        },

        error: (error: HttpErrorResponse) => {
          loadingModalRef.close();

          console.error(error);

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
    }
  )
  }

  changeStatus(nextStatus: TicketStatusEnum): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    const ticketId = this.ticketForm.get("id")?.value;

    this.ticketsService.updateStatus(ticketId, nextStatus).subscribe({
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

  getTicketStatus(): TicketStatusEnum | "Novo Chamado" {
    return this.ticketForm.get('status')?.value || "Novo Chamado";
  }

  enableFormEditing(): void {
    this.ticketForm.enable();
    this.formIsBeingEdited = true;
  }

  canEditTicket(): boolean {
    const userId: string = this.loggedUserDetailsService.getUserId();
    const technicalId: string = this.ticketForm.get("technical")?.value;
    const customerId: string = this.ticketForm.get("customer")?.value;
    const isNotFinishedStatus: boolean = this.ticketForm.get("status")?.value !== TicketStatusEnum.RESOLVED;

    return this.isDetailsForm && isNotFinishedStatus && userId === technicalId || userId === customerId;
  }

  return(): void {
    this.location.back();
  }
}
