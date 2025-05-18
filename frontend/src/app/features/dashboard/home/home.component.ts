import { Component } from '@angular/core';
import { LoggedUserService } from 'src/app/services/logged-user-details/logged-user-details.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  constructor(
    private loggedUserService: LoggedUserService
  ){}

  getUserName(): string {
    return this.loggedUserService.getUserDetails().name;
  }
}
