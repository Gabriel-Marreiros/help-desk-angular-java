import { Location } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { LoggedUserDetailsService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { GenericModalComponent } from 'src/app/shared/generic-modal/generic-modal.component';
import { LoadingModalComponent } from 'src/app/shared/loading-modal/loading-modal.component';
import { UserStatusEnum } from 'src/app/typings/enums/user-status.enum';

@Component({
  selector: 'app-technical-form',
  templateUrl: './technical-form.component.html',
  styleUrls: ['./technical-form.component.scss']
})
export class TechnicalFormComponent implements OnInit {

  technicalDefaultProfileAvatar: string = 'assets/images/technical-default-profile-avatar.png';

  technicalForm: FormGroup = new FormGroup({
    technicalId: new FormControl(),
    userId: new FormControl(),
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
    dateBirth: new FormControl('', [Validators.required]),
    phoneNumber: new FormControl('', [Validators.required]),
    profilePicture: new FormControl(this.technicalDefaultProfileAvatar),
    userStatus: new FormControl()
  })

  isDetailView: boolean = false;
  formIsBeingEdited: boolean = false;
  hidePassword: boolean = true;
  technicalIsActive: boolean = false;

  @ViewChild("profilePictureInput")
  profilePictureInput!: ElementRef<HTMLInputElement>;

  constructor(
    private technicalService: TechnicalService,
    protected loggedUserDetailsService: LoggedUserDetailsService,
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private location: Location
  ){}

  ngOnInit(): void {
    const technicalId = this.route.snapshot.paramMap.get('id');
    technicalId && this.getTechnicalDetail(technicalId);
  }

  private getTechnicalDetail(technicalId: string): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.technicalService.geTechnicalById(technicalId).subscribe({
      next: (data) => {
        this.technicalForm.patchValue({
          technicalId: data.body?.technicalId,
          userId: data.body?.userId,
          name: data.body?.name,
          email: data.body?.email,
          dateBirth: data.body?.dateBirth,
          phoneNumber: data.body?.phoneNumber,
          profilePicture: data.body?.profilePicture,
          userStatus: data.body?.userStatus,
        })

        this.technicalIsActive = data.body?.userStatus == UserStatusEnum.ACTIVE;

        this.technicalForm.disable();
        this.isDetailView = true;

        loadingModalRef.close();
      }
    })
  }


  saveTechnical(): void {
    if(this.technicalForm.invalid) return;

    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.technicalService.saveTechnical(this.technicalForm.value).subscribe({
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


  enableFormEdit(): void {
    this.technicalForm.enable();
    this.technicalForm.get('password')?.clearValidators();
    this.technicalForm.get('password')?.updateValueAndValidity();
    this.formIsBeingEdited = true;
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

  changeTechnicalActiveStatus(): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    const technicalId: string = this.technicalForm.get("technicalId")?.value;

    this.technicalService.changeTechnicalActiveStatus(technicalId).subscribe({
      next: (response) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = `Técnico ${this.technicalIsActive ? "inativado": "ativado"} com sucesso!`;
        genericModalRef.componentInstance.redirectLink = "dashboard/tecnicos";
      },

      error: (error: HttpErrorResponse) => {
        console.error(error);

        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = `Ocorreu um erro ao ${this.technicalIsActive ? "inativar": "ativar"} o tecnico! Por favor, entre em contato com o departamento de TI ou tente novamente mais tarde!`;
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
      this.technicalForm.get('profilePicture')?.setValue(this.technicalDefaultProfileAvatar);
      return;
    }

    const file: File = files.item(0)!;

    const fileReader: FileReader = new FileReader();

    fileReader.readAsDataURL(file);

    fileReader.onload = () => {
      this.technicalForm.get("profilePicture")?.setValue(fileReader.result);
    }
  }

  canUpdate(): boolean {
    const loggedUserId: string = this.loggedUserDetailsService.getUserId();
    const userId: string = this.technicalForm.get("userId")?.value;

    return this.loggedUserDetailsService.isAdmin() || loggedUserId === userId;
  }

  return(): void {
    this.location.back();
  }
}
