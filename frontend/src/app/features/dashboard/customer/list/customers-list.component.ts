import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { LoggedUserDetailsService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { CustomerModel } from 'src/app/typings/models/customer.model';

@Component({
  selector: 'app-customers-list',
  templateUrl: './customers-list.component.html',
  styleUrls: ['./customers-list.component.scss']
})
export class CustomersListComponent implements OnInit{

  $customers = new BehaviorSubject<Array<CustomerModel>>([]);;
  page: number = 0;
  size: number = 3;
  isFetchingData: boolean = false;
  finishedPages: boolean = false;

  constructor(
    private customerService: CustomerService,
    private currentRoute: ActivatedRoute,
    private loggedUserDetailsService: LoggedUserDetailsService,
    private router: Router
  ){}

  ngOnInit(): void {
    this.getCustomersPaginated();
  }

  openCustomerDetails(customer: CustomerModel): void {
    this.router.navigate(['detalhes', customer.customerId], {
      relativeTo: this.currentRoute
    });
  }

  getCustomersPaginated(){
    if(this.finishedPages) return;

    this.isFetchingData = true;

    this.customerService.getCustomersPaginated(this.page, this.size).subscribe({
      next: (response) => {
        this.finishedPages = response.last;
        const newValue: Array<CustomerModel> = this.$customers.value.concat(response.content);
        this.$customers.next(newValue);
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
