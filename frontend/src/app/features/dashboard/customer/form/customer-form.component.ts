import { Location } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { LoggedUserDetailsService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { GenericModalComponent } from 'src/app/shared/generic-modal/generic-modal.component';
import { LoadingModalComponent } from 'src/app/shared/loading-modal/loading-modal.component';
import { UserStatusEnum } from 'src/app/typings/enums/user-status.enum';

@Component({
  selector: 'app-customer-form',
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.scss']
})
export class CustomerFormComponent {

  customerDefaultProfileAvatar: string = 'assets/images/customer-default-profile-avatar.png';

  customerForm: FormGroup = new FormGroup({
    customerId: new FormControl(),
    userId: new FormControl(),
    name: new FormControl('', [Validators.required]),
    cnpj: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
    phoneNumber: new FormControl('', [Validators.required]),
    profilePicture: new FormControl(this.customerDefaultProfileAvatar),
    userStatus: new FormControl()
  });

  isDetailView: boolean = false;
  formIsBeingEdited: boolean = false;
  hidePassword: boolean = false;
  customerIsActive: boolean = false;

  @ViewChild("customerProfilePictureInput")
  profilePictureInput!: ElementRef<HTMLInputElement>;

  constructor(
    private customerService: CustomerService,
    protected loggedUserDetailsService: LoggedUserDetailsService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private location: Location
  ) { }

  ngOnInit(): void {
    const customerId: string | null = this.route.snapshot.paramMap.get('id');
    customerId && this.loadCustomerDetails(customerId);
  }


  private loadCustomerDetails(customerId: string){
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.customerService.getCustomerById(customerId).subscribe({
      next: (data) => {
        this.customerForm.patchValue({
          customerId: data.body?.customerId,
          userId: data.body?.userId,
          name: data.body?.name,
          cnpj: data.body?.cnpj,
          email: data.body?.email,
          phoneNumber: data.body?.phoneNumber,
          userStatus: data.body?.userStatus,
          profilePicture: data.body?.profilePicture
        });

        this.customerIsActive = data.body?.userStatus == UserStatusEnum.ACTIVE;

        this.customerForm.disable();
        this.isDetailView = true;

        loadingModalRef.close();
      }
    })
  }

  public enableFormEdit(): void {
    this.customerForm.enable();
    this.customerForm.get('password')?.clearValidators();
    this.customerForm.get('password')?.updateValueAndValidity();
    this.formIsBeingEdited = true;
  }

  public saveCustomer(): void {
    if(this.customerForm.invalid) return;

    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.customerService.saveCustomer(this.customerForm.value).subscribe({
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

  changeCustomerActiveStatus(): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    const customerId: string = this.customerForm.get("userId")?.value;

    this.customerService.changeCustomerActiveStatus(customerId).subscribe({
      next: (response) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = `Cliente ${this.customerIsActive ? "inativado": "ativado"} com sucesso!`;
        genericModalRef.componentInstance.redirectLink = "dashboard/clientes";
      },

      error: (error: HttpErrorResponse) => {
        console.error(error);

        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = `Ocorreu um erro ao ${this.customerIsActive ? "inativar": "ativar"} o cliente! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!`;
        genericModalRef.componentInstance.redirectLink = "dashboard/home";
      }
    })
  }

  activeProfilePictureInput(): void {
    this.profilePictureInput.nativeElement.click();
  }

  handleProfilePictureInput(): void {
    const files: FileList | null = this.profilePictureInput.nativeElement.files;

    if(!files?.length){
      this.customerForm.get('profilePicture')?.setValue(this.customerDefaultProfileAvatar);
      return;
    }

    const file: File = files.item(0)!;

    const fileReader: FileReader = new FileReader();

    fileReader.readAsDataURL(file);

    fileReader.onload = () => {
      this.customerForm.get("profilePicture")?.setValue(fileReader.result);
    }
  }

  canUpdate(): boolean {
    const loggedUserId: string = this.loggedUserDetailsService.getUserId();
    const userId: string = this.customerForm.get("userId")?.value;

    return this.loggedUserDetailsService.isAdmin() || loggedUserId === userId;
  }

  return(): void {
    this.location.back();
  }
}
