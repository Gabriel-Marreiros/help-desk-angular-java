import { Component } from '@angular/core';
import { faGithub } from '@fortawesome/free-brands-svg-icons';
import { faHouse, faScrewdriverWrench, faSuitcase, faTicket } from '@fortawesome/free-solid-svg-icons';
import { LoggedUserDetailsService } from 'src/app/services/logged-user-details/logged-user-details.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {

  faHouse = faHouse;
  faGithub = faGithub;
  faTicket = faTicket;
  faSuitcase = faSuitcase;
  faScrewdriverWrench = faScrewdriverWrench

  constructor(
    private loggedUserDetailsService: LoggedUserDetailsService
  ){}

  canViewButtonNewTicket(): boolean {
    return this.loggedUserDetailsService.isCustomer() || this.loggedUserDetailsService.isAdmin();
  }

}
