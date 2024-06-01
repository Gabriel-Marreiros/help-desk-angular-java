import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, firstValueFrom } from 'rxjs';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {

  private authorizationTokenName: string = environment.authorizationTokenName;

  constructor(
    private authService: AuthenticationService,
    private router: Router
  ){}

  async canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    const token: string | null = localStorage.getItem(this.authorizationTokenName);

    if(!token){
      this.router.navigate(["login"]);
      return false;
    }

    const validity: boolean = await firstValueFrom(this.authService.validateToken(token))

    if(!validity){
      this.router.navigate(["login"]);
      return false;
    }

    return true;
  }

}
