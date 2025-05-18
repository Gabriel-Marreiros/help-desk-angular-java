import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { LoggedUserService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { GenericModalComponent } from 'src/app/shared/components/generic-modal/generic-modal.component';
import { LoadingModalComponent } from 'src/app/shared/components/loading-modal/loading-modal.component';
import { UserStatusEnum } from 'src/app/typings/enums/user-status.enum';

@Component({
  selector: 'app-technical-form',
  templateUrl: './technical-form.component.html',
  styleUrls: ['./technical-form.component.scss']
})
export class TechnicalFormComponent implements OnInit {

  technicalDefaultProfileAvatar: string = 'assets/images/technical-default-profile-avatar.png';

  technicalForm: FormGroup = new FormGroup({
    id: new FormControl(),
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
    dateBirth: new FormControl('', [Validators.required]),
    phoneNumber: new FormControl('', [Validators.required]),
    profilePicture: new FormControl(this.technicalDefaultProfileAvatar),
    role: new FormControl('', [Validators.required]),
    userStatus: new FormControl()
  })

  isDetailView: boolean = false;
  hidePassword: boolean = true;

  constructor(
    private technicalService: TechnicalService,
    private authenticationService: AuthenticationService,
    protected loggedUserService: LoggedUserService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ){}

  ngOnInit(): void {
    const technicalId = this.route.snapshot.params["id"];

    if(technicalId){
      this.getTechnicalDetail(technicalId);
    }
  }

  private getTechnicalDetail(technicalId: string): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.technicalService.geTechnicalById(technicalId).subscribe({
      next: (data) => {
        this.technicalForm.patchValue({
          id: data.body?.id,
          name: data.body?.name,
          email: data.body?.email,
          dateBirth: data.body?.dateBirth,
          phoneNumber: data.body?.phoneNumber,
          profilePicture: data.body?.profilePicture,
          role: data.body?.role,
          userStatus: data.body?.userStatus,
        })

        this.technicalForm.disable();
        this.isDetailView = true;

        loadingModalRef.close();
      },

      error: (error) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)

        if(error.status === HttpStatusCode.NotFound){
          genericModalRef.componentInstance.contentMessage = "Técnico não encontrado!";
          genericModalRef.componentInstance.redirectLink = "dashboard/tecnicos";
        }
      }
    })
  }


  saveTechnical(): void {
    if(this.technicalForm.invalid) return;

    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.authenticationService.registerTechnical(this.technicalForm.value).subscribe({
      next: (response) => {
        loadingModalRef.close();

        const genericModalRef= this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Técnico cadastrado com sucesso!";
        genericModalRef.componentInstance.redirectLink = "dashboard/tecnicos";
      },

      error: (error) => {
        console.error(error)

        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao cadastrar o técnico! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!";
        genericModalRef.componentInstance.redirectLink = "dashboard/home";
      }
    });
  }

  updateTechnical(): void {
    if(this.technicalForm.invalid) return;

    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.technicalService.updateTechnical(this.technicalForm.value).subscribe({
      next: (response) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Técnico atualizado com sucesso!";
        genericModalRef.componentInstance.redirectLink = "dashboard/tecnicos";
      },

      error: (error: HttpErrorResponse) => {
        console.error(error);

        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao atualizar os dados do técnico! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!";
        genericModalRef.componentInstance.redirectLink = "dashboard/home";
      }
    })
  }

  changeTechnicalStatus(newStatus: UserStatusEnum): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    const technicalId: string = this.technicalForm.get("id")?.value;

    this.technicalService.changeTechnicalActiveStatus(technicalId).subscribe({
      next: (response) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = `Técnico ${newStatus == UserStatusEnum.ACTIVE ? "ativado": "inativado"} com sucesso!`;
        genericModalRef.componentInstance.redirectLink = "dashboard/tecnicos";
      },

      error: (error: HttpErrorResponse) => {
        console.error(error);

        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = `Ocorreu um erro ao ${newStatus == UserStatusEnum.ACTIVE ? "ativar": "inativar"} o tecnico! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!`;
        genericModalRef.componentInstance.redirectLink = "dashboard/home";
      }
    })
  }
}
