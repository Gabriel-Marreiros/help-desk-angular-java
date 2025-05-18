import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { RolesEnum } from 'src/app/typings/enums/roles.enum';
import { ILoggedUser } from 'src/app/typings/interfaces/logged-user-details';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoggedUserService {

  private userDetails!: ILoggedUser;
  private authorizationTokenName: string = environment.authorizationTokenName;

  constructor(
    private jwtHelper: JwtHelperService
  ) {
    this.setUserDetailsByToken();
  }

  setUserDetailsByToken(token?: string | null): void {
    token ??= localStorage.getItem(this.authorizationTokenName);

    this.userDetails = this.jwtHelper.decodeToken(token!)!;
  }

  getUserId(): string {
    return this.userDetails?.id!;
  }

  getUserDetails(): ILoggedUser {
    return this.userDetails;
  }

  isAdmin(): boolean {
    return this.userDetails?.role == RolesEnum.ADMIN;
  }

  isCustomer(): boolean {
    return this.userDetails?.role == RolesEnum.CUSTOMER;
  }

  isTechnical(): boolean {
    return this.userDetails?.role == RolesEnum.TECHNICAL;
  }

}
