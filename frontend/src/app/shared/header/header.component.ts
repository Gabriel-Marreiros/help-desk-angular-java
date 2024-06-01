import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { LoggedUserDetailsService } from 'src/app/services/logged-user-details/logged-user-details.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  userDetails: any;

  constructor(
    private loggedUserDetailsService: LoggedUserDetailsService,
    private authenticationService: AuthenticationService
  ){}

  ngOnInit(): void {
    this.userDetails = this.loggedUserDetailsService.getUserDetails();
  }

  logout(): void {
    this.authenticationService.logout();
  }

}
