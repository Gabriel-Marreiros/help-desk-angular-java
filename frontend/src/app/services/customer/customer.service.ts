import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IPage } from 'src/app/typings/interfaces/page';
import { CustomerModel } from 'src/app/typings/models/customer.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private readonly BASE_URL: string = `${environment.apiUrl}/customers`;

  constructor(
    private http: HttpClient
  ){}

  getAllCustomers(): Observable<HttpResponse<Array<CustomerModel>>> {
    return this.http.get<Array<CustomerModel>>(this.BASE_URL, {
      observe: 'response',
      responseType: 'json'
    })
  }

  getCustomersPaginated(params: {page: number, size: number, status?: string, search?: string}): Observable<IPage<CustomerModel>> {
    const url: string = `${this.BASE_URL}/paginated`;

    return this.http.get<IPage<CustomerModel>>(url, {
      params
    });
  }

  getCustomerById(id: string): Observable<HttpResponse<CustomerModel>> {
    const url: string = `${this.BASE_URL}/${id}`;
    return this.http.get<CustomerModel>(url, {
      observe: 'response',
      responseType: 'json'
    });
  }

  updateCustomer(updatedCustomer: Partial<CustomerModel>): Observable<HttpResponse<CustomerModel>> {
    const url = `${this.BASE_URL}/${updatedCustomer.id}`;

    return this.http.put<CustomerModel>(url, updatedCustomer, {
      observe: 'response',
      responseType: 'json'
    })
  }

  changeCustomerActiveStatus(id: string): Observable<void> {
    const url: string = `${this.BASE_URL}/${id}`;

    return this.http.delete<void>(url);
  }
}
