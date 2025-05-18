import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { RolesEnum } from 'src/app/typings/enums/roles.enum';
import { UserModel } from 'src/app/typings/models/user.model';

@Component({
  selector: 'app-user-profile-button',
  templateUrl: './user-profile-button.component.html',
  styleUrls: ['./user-profile-button.component.scss']
})
export class UserProfileButtonComponent {
  @Input()
  userRole!: RolesEnum;

  @Input()
  userId!: string;

  @Input()
  userName!: string;

  @Input()
  profilePicture!: string;

  @Input()
  textColor: 'text-black' | 'text-white' = 'text-black';

  constructor(
    private router: Router
  ){}

  openUserDetails(): void {
    let formType: string = this.userRole == RolesEnum.TECHNICAL ? "tecnicos" : "clientes";

    this.router.navigate(["dashboard", formType, "detalhes", this.userId]);
  }
}
