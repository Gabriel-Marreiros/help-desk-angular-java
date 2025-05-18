import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { LoggedUserService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { UserStatusEnum } from 'src/app/typings/enums/user-status.enum';
import { CustomerModel } from 'src/app/typings/models/customer.model';

@Component({
  selector: 'app-customers-list',
  templateUrl: './customers-list.component.html',
  styleUrls: ['./customers-list.component.scss']
})
export class CustomersListComponent implements OnInit{

  customers$ = new BehaviorSubject<Array<CustomerModel>>([]);;
  page: number = 0;
  size: number = 6;
  isFetchingData: boolean = false;
  finishedPages: boolean = false;
  userStatusEnum: typeof UserStatusEnum = UserStatusEnum;
  statusFilter!: string;
  searchFilter!: string;

  constructor(
    private customerService: CustomerService,
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
      this.customers$.next([]);

      this.getCustomersPaginated();
    })
  }

  openCustomerDetails(customer: CustomerModel): void {
    this.router.navigate(['detalhes', customer.id], {
      relativeTo: this.currentRoute
    });
  }

  getCustomersPaginated(){
    if(this.finishedPages) return;

    this.isFetchingData = true;

    const params: {page: number, size: number, status?: string, search?: string} = {
      page: this.page,
      size: this.size,
    }

    this.statusFilter && (params["status"] = this.statusFilter);
    this.searchFilter && (params["search"] = this.searchFilter);

    this.customerService.getCustomersPaginated(params).subscribe({
      next: (response) => {
        this.finishedPages = response.last;
        const newValue: Array<CustomerModel> = this.customers$.value.concat(response.content);
        this.customers$.next(newValue);
        this.page++;
        this.isFetchingData = false;
      },

      error: (error) => {
        console.error(error);
      }
    });
  }

  handleSelectFilter(filter: Record<string, string>): void {
    this.router.navigate([], {
      queryParams: filter,
      queryParamsHandling: 'merge'
    });
  }

  userIsAdmin(): boolean {
    return this.loggedUserService.isAdmin();
  }

}
