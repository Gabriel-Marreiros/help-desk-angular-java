import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { RolesEnum } from 'src/app/typings/enums/roles.enum';
import { CustomerModel } from 'src/app/typings/models/customer.model';
import { TechnicalModel } from 'src/app/typings/models/technical.model';

@Component({
  selector: 'app-user-profile-button',
  templateUrl: './user-profile-button.component.html',
  styleUrls: ['./user-profile-button.component.scss']
})
export class UserProfileButtonComponent {

  @Input()
  userDetails!: TechnicalModel | CustomerModel;

  @Input()
  textColor: 'text-black' | 'text-white' = 'text-black';

  constructor(
    private router: Router
  ){}

  openUserDetails(): void {
    let formType: string = this.userDetails.role.title == RolesEnum.TECHNICAL ? "tecnicos" : "clientes";
    let userId: string = this.userDetails.userId!;

    this.router.navigate(["dashboard", formType, "detalhes", userId]);
  }
}
