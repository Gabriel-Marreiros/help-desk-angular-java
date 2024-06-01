import { HttpErrorResponse, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { LoggedUserDetailsService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { LoadingModalComponent } from 'src/app/shared/loading-modal/loading-modal.component';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {

  loginForm: FormGroup = new FormGroup({
    email: new FormControl("",[Validators.email, Validators.required]),
    password: new FormControl("", [Validators.required])
  })

  currentYear: number = new Date().getFullYear();
  hidePasswordText: boolean = true;
  invalidLogin: boolean = false;

  authorizationTokenName: string = environment.authorizationTokenName;

  constructor(
    private authService: AuthenticationService,
    private loggedUserDetailsService: LoggedUserDetailsService,
    private dialog: MatDialog,
    private router: Router
  ){}


  ngOnInit(): void {
    this.checkUserAlreadyLoggedin();
  }

  checkUserAlreadyLoggedin(){
    const token: string | null = localStorage.getItem(this.authorizationTokenName);

    if(token){
      this.authService.validateToken(token).subscribe({
        next: (validated: boolean) => {
          if(validated){
            this.router.navigate(["dashboard", "home"])
          }
        }
      })
    }
  }

  login($event: SubmitEvent): void {
    $event.preventDefault();

    if(this.loginForm.invalid) return;

    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.authService.login(this.loginForm.value).subscribe({
      next: (response) => {
        if(response.status == HttpStatusCode.Ok){
          localStorage.setItem(this.authorizationTokenName, response.body!.token);
          this.loggedUserDetailsService.setUserDetailsByToken(response.body!.token);
          this.router.navigate(["dashboard"]);
          loadingModalRef.close();
        }
      },

      error: (error: HttpErrorResponse) => {
        if(error.status == HttpStatusCode.Forbidden){
          this.invalidLogin = true;
          loadingModalRef.close();
        }
      }
    })
  }

  showControlError(formControl: string, error: 'required' | 'email'): boolean {
    return this.loginForm.get(formControl)?.errors?.[error];
  }
}
