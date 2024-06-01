import { Component } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';

@Component({
  selector: 'app-unauthorized-page',
  templateUrl: './unauthorized-page.component.html',
  styleUrls: ['./unauthorized-page.component.scss']
})
export class UnauthorizedPageComponent {

  constructor(protected authenticationService: AuthenticationService){}
}
