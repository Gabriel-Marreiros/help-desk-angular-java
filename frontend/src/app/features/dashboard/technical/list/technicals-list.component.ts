import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { LoggedUserService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { UserStatusEnum } from 'src/app/typings/enums/user-status.enum';
import { ITechnicalWithTicketStatusCount } from 'src/app/typings/interfaces/technical-with-ticket-status-count';

@Component({
  selector: 'app-technicals-list',
  templateUrl: './technicals-list.component.html',
  styleUrls: ['./technicals-list.component.scss']
})
export class TechnicalsListComponent implements OnInit {

  technicians$ = new BehaviorSubject<Array<ITechnicalWithTicketStatusCount>>([]);
  userStatusEnum: typeof UserStatusEnum = UserStatusEnum;
  page: number = 0;
  size: number = 6;
  isFetchingData: boolean = false;
  finishedPages: boolean = false;

  statusFilter!: string;
  searchFilter!: string;

  constructor(
    private technicalService: TechnicalService,
    private currentRoute: ActivatedRoute,
    private loggedUserService: LoggedUserService,
    private router: Router
  ){}

  ngOnInit(): void {
    this.currentRoute.queryParams.subscribe(({status, search}) => {
      this.statusFilter = status;
      this.searchFilter = search;

      this.page = 0;
      this.finishedPages = false;
      this.technicians$.next([]);

      this.getTechniciansPaginated();
    })
  }


  openTechnicalDetails(technical: ITechnicalWithTicketStatusCount): void {
    this.router.navigate(['detalhes', technical.id], {
      relativeTo: this.currentRoute
    });
  }

  getTechniciansPaginated(){
    if(this.finishedPages) return;

    this.isFetchingData = true;

    const params: {page: number, size: number, status?: string, search?: string} = {
      page: this.page,
      size: this.size,
    }

    this.statusFilter && (params.status = this.statusFilter);
    this.searchFilter && (params.search = this.searchFilter);

    this.technicalService.getTechniciansWithTicketsStatusCountPaginated(params).subscribe({
      next: (response) => {
        this.finishedPages = response.last;
        const newValue: Array<ITechnicalWithTicketStatusCount> = this.technicians$.value.concat(response.content);
        this.technicians$.next(newValue);
        this.page++;
        this.isFetchingData = false;
      },

      error: (error) => {
        console.error(error);
      }
    });
  }

  userIsAdmin(): boolean {
    return this.loggedUserService.isAdmin();
  }

  handleSelectFilter(filter: Record<string, string>): void {
    this.router.navigate([], {
      queryParams: filter,
      queryParamsHandling: 'merge'
    });
  }

}
