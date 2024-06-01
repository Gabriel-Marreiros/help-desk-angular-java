import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { LoggedUserDetailsService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { UserStatusEnum } from 'src/app/typings/enums/user-status.enum';
import { ITechnicalWithTicketStatusCount } from 'src/app/typings/interfaces/technical-with-ticket-status-count';

@Component({
  selector: 'app-technicals-list',
  templateUrl: './technicals-list.component.html',
  styleUrls: ['./technicals-list.component.scss']
})
export class TechnicalsListComponent implements OnInit {

  $technicians = new BehaviorSubject<Array<ITechnicalWithTicketStatusCount>>([]);
  userStatusEnum: typeof UserStatusEnum = UserStatusEnum;
  page: number = 0;
  size: number = 3;
  isFetchingData: boolean = false;
  finishedPages: boolean = false;

  constructor(
    private technicalService: TechnicalService,
    private currentRoute: ActivatedRoute,
    private loggedUserDetailsService: LoggedUserDetailsService,
    private router: Router
  ){}

  ngOnInit(): void {
    this.getTechniciansPaginated();
  }


  openTechnicalDetails(Technical: any): void {
    this.router.navigate(['detalhes', Technical.id], {
      relativeTo: this.currentRoute
    });
  }

  getTechniciansPaginated(){
    if(this.finishedPages) return;

    this.isFetchingData = true;

    this.technicalService.getTechniciansWithTicketsStatusCountPaginated(this.page, this.size).subscribe({
      next: (response) => {
        this.finishedPages = response.last;
        const newValue: Array<ITechnicalWithTicketStatusCount> = this.$technicians.value.concat(response.content);
        this.$technicians.next(newValue);
        this.page++;
        this.isFetchingData = false;
      },

      error: (error) => {
        console.error(error);
      }
    });
  }

  userIsAdmin(): boolean {
    return this.loggedUserDetailsService.isAdmin();
  }

}
