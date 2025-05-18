import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { LoggedUserService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { GenericModalComponent } from 'src/app/shared/components/generic-modal/generic-modal.component';
import { LoadingModalComponent } from 'src/app/shared/components/loading-modal/loading-modal.component';
import { UserStatusEnum } from 'src/app/typings/enums/user-status.enum';

@Component({
  selector: 'app-customer-form',
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.scss']
})
export class CustomerFormComponent {

  customerDefaultProfileAvatar: string = 'assets/images/customer-default-profile-avatar.png';

  customerForm: FormGroup = new FormGroup({
    id: new FormControl(),
    name: new FormControl('', [Validators.required]),
    cnpj: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
    phoneNumber: new FormControl('', [Validators.required]),
    profilePicture: new FormControl(this.customerDefaultProfileAvatar),
    role: new FormControl('', [Validators.required]),
    userStatus: new FormControl()
  });

  isDetailView: boolean = false;
  hidePassword: boolean = false;

  constructor(
    private customerService: CustomerService,
    private authenticationService: AuthenticationService,
    protected loggedUserService: LoggedUserService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    const customerId: string | null = this.route.snapshot.params["id"];

    if(customerId){
      this.loadCustomerDetails(customerId);
    }
  }


  private loadCustomerDetails(customerId: string){
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.customerService.getCustomerById(customerId).subscribe({
      next: (data) => {
        this.customerForm.patchValue({
          id: data.body?.id,
          name: data.body?.name,
          cnpj: data.body?.cnpj,
          email: data.body?.email,
          phoneNumber: data.body?.phoneNumber,
          role: data.body?.role,
          userStatus: data.body?.userStatus,
          profilePicture: data.body?.profilePicture
        });

        this.isDetailView = true;
        this.customerForm.disable();

        loadingModalRef.close();
      },

      error: (error) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)

        if(error.status === HttpStatusCode.NotFound){
          genericModalRef.componentInstance.contentMessage = "Cliente não encontrado!";
          genericModalRef.componentInstance.redirectLink = "dashboard/clientes";
        }
      }
    })
  }

  public saveCustomer(): void {
    if(this.customerForm.invalid) return;

    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.authenticationService.registerCustomer(this.customerForm.value).subscribe({
      next: (response) => {
        loadingModalRef.close();

        const genericModalRef= this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Cliente cadastrado com sucesso!";
        genericModalRef.componentInstance.redirectLink = "dashboard/clientes";
      },

      error: (error) => {
        console.error(error);

        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao cadastrar o cliente! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!";
        genericModalRef.componentInstance.redirectLink = "dashboard/home";
      }
    });
  }

  public updateCustomer(): void {
    if(this.customerForm.invalid) {
      return;
    };

    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.customerService.updateCustomer(this.customerForm.value).subscribe({
      next: (response) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Cliente atualizado com sucesso!";
        genericModalRef.componentInstance.redirectLink = "dashboard/clientes";
      },

      error: (error: HttpErrorResponse) => {
        console.error(error);

        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao atualizar os dados do cliente! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!";
        genericModalRef.componentInstance.redirectLink = "dashboard/home";
      }
    })
  }

  changeCustomerStatus(newStatus: UserStatusEnum): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    const customerId: string = this.customerForm.get("id")?.value;

    this.customerService.changeCustomerActiveStatus(customerId).subscribe({
      next: (response) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = `Cliente ${newStatus == UserStatusEnum.ACTIVE ? "ativado": "inativado"} com sucesso!`;
        genericModalRef.componentInstance.redirectLink = "dashboard/clientes";
      },

      error: (error: HttpErrorResponse) => {
        console.error(error);

        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = `Ocorreu um erro ao ${newStatus == UserStatusEnum.ACTIVE ? "ativar": "inativar"} o cliente! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!`;
        genericModalRef.componentInstance.redirectLink = "dashboard/home";
      }
    })
  }
}
