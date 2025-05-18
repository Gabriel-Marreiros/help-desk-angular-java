import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { LoggedUserService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { ILoggedUser } from 'src/app/typings/interfaces/logged-user-details';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  userDetails!: ILoggedUser;

  constructor(
    private loggedUserService: LoggedUserService,
    private authenticationService: AuthenticationService
  ){}

  ngOnInit(): void {
    this.userDetails = this.loggedUserService.getUserDetails();
  }

  logout(): void {
    this.authenticationService.logout();
  }

}
