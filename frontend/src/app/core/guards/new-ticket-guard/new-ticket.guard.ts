import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoggedUserService } from 'src/app/services/logged-user-details/logged-user-details.service';

@Injectable({
  providedIn: 'root'
})
export class NewTicketGuard implements CanActivate {

  constructor(
    private loggedUserService: LoggedUserService,
    private router: Router
  ){}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const isUserOrAdmin: boolean = this.loggedUserService.isCustomer() || this.loggedUserService.isAdmin();

    if(isUserOrAdmin){
      return true;
    }
    else {
      this.router.navigate(["dashboard", "chamados"]);
      return false;
    }
  }

}
