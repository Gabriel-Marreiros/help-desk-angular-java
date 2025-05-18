import { HttpErrorResponse, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatRadioChange } from '@angular/material/radio';
import { Router } from '@angular/router';
import { Observer } from 'rxjs';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { GenericModalComponent } from 'src/app/shared/components/generic-modal/generic-modal.component';
import { LoadingModalComponent } from 'src/app/shared/components/loading-modal/loading-modal.component';
import { CustomerModel } from 'src/app/typings/models/customer.model';
import { TechnicalModel } from 'src/app/typings/models/technical.model';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.scss']
})
export class RegisterFormComponent implements OnInit {

  commonControls = {
    name: new FormControl<string>("", [Validators.required, Validators.maxLength(35)]),
    email: new FormControl<string>("", [Validators.email, Validators.required]),
    password: new FormControl<string>("", [Validators.required]),
    phoneNumber: new FormControl<string>("", [Validators.required]),
  };

  customerControls = {
    cnpj: new FormControl<string>("", [Validators.required])
  };

  technicalControls = {
    dateBirth: new FormControl<string>("", [Validators.required]),
  };

  userProfile: "customer" | "technical" | null = null;

  registerForm: FormGroup = this.formBuilder.group(this.commonControls);

  currentYear: number = new Date().getFullYear();
  hidePasswordText: boolean = true;
  invalidLogin: boolean = false;
  serverError: boolean = false;
  showUserProfileError: boolean = false;

  authorizationTokenName: string = environment.authorizationTokenName;

  constructor(
    private authService: AuthenticationService,
    private dialog: MatDialog,
    private router: Router,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    this.checkUserAlreadyLoggedin();
  }

  checkUserAlreadyLoggedin() {
    const token: string | null = localStorage.getItem(this.authorizationTokenName);

    if (token) {
      this.authService.validateToken(token).subscribe({
        next: (validated: boolean) => {
          if (validated) {
            this.router.navigate(["dashboard", "home"])
          }
        }
      })
    }
  }

  register($event: SubmitEvent): void {
    $event.preventDefault();

    if(!this.userProfile){
      this.showUserProfileError = true;
      return;
    }

    if (this.registerForm.invalid) {
      return;
    };

    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    const subscription: Partial<Observer<HttpResponse<CustomerModel | TechnicalModel>>> = {
      next: (response) => {
        if (response.status == HttpStatusCode.Created) {
          loadingModalRef.close();

          const genericModalRef = this.dialog.open(GenericModalComponent)
          genericModalRef.componentInstance.contentMessage = "Usuário cadastrado com sucesso!";
          genericModalRef.componentInstance.redirectLink = "/login";
        }
      },

      error: (error: HttpErrorResponse) => {
        loadingModalRef.close();

        const genericModalRef = this.dialog.open(GenericModalComponent)
        genericModalRef.componentInstance.contentMessage = "Ocorreu um erro ao cadastrar o usuário. Por favor, tente novamente mais tarde!";
        genericModalRef.componentInstance.redirectLink = "/login";
      }
    }

    const profile: "customer" | "technical" = this.registerForm.get("profile")?.value;

    switch (profile) {
      case 'customer': {
        this.authService.registerCustomer(this.registerForm.value).subscribe(subscription);
        break;
      }

      case 'technical': {
        this.authService.registerTechnical(this.registerForm.value).subscribe(subscription);
        break;
      }
    }
  }

  showControlError(formControl: string, error: keyof ValidationErrors): boolean {
    return this.registerForm.get(formControl)?.errors?.[error];
  }

  isTechnicalProfile(): boolean {
    return this.userProfile === "technical";
  }

  isCustomerProfile(): boolean {
    return this.userProfile === "customer";
  }

  handleUserProfileChange(event: MatRadioChange): void {
    this.userProfile = event.value;

    switch (this.userProfile) {
      case 'customer': {
        this.registerForm = this.formBuilder.group({ ...this.commonControls, ...this.customerControls });
        break;
      }

      case 'technical': {
        this.registerForm = this.formBuilder.group({ ...this.commonControls, ...this.technicalControls });
        break;
      }
    }
  }
}
