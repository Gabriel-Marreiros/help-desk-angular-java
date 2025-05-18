import { Location } from '@angular/common';
import { Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { LoggedUserService } from 'src/app/services/logged-user-details/logged-user-details.service';
import { RolesEnum } from 'src/app/typings/enums/roles.enum';
import { UserStatusEnum } from 'src/app/typings/enums/user-status.enum';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnChanges {

  @Input("formGroup")
  userForm!: FormGroup;

  @Input()
  isDetailView: boolean = false;

  @Output()
  onSaveUser: EventEmitter<null> = new EventEmitter();

  @Output()
  onUpdateUser: EventEmitter<null> = new EventEmitter();

  @Output()
  onChangeUserStatus: EventEmitter<UserStatusEnum > = new EventEmitter();

  @ViewChild("profilePictureInput")
  profilePictureInput!: ElementRef<HTMLInputElement>;

  userStatus: typeof UserStatusEnum = UserStatusEnum;
  formIsBeingEdited: boolean = false;
  userIsActive: boolean = true;

  constructor(
    private loggedUserService: LoggedUserService,
    private location: Location
  ){}

  ngOnChanges(changes: SimpleChanges): void {
    if(!changes["userForm"]?.currentValue){
      this.userIsActive = this.userForm.get("userStatus")?.value === UserStatusEnum.ACTIVE;
    }
  }

  userIsAdmin(): boolean {
    return this.loggedUserService.isAdmin();
  }


  activeProfilePictureInput(): void {
    this.profilePictureInput.nativeElement.click();
  }

  handleProfilePictureInput(): void {
    const files: FileList | null = this.profilePictureInput.nativeElement.files;

    if(!files?.length){
      return;
    }

    const file: File = files.item(0)!;

    const fileReader: FileReader = new FileReader();

    fileReader.readAsDataURL(file);

    fileReader.onload = () => {
      this.userForm.get("profilePicture")?.setValue(fileReader.result);
    }
  }

  canEditUser(): boolean {
    const loggedUserId: string = this.loggedUserService.getUserId();
    const userId: string = this.userForm.get("id")?.value;

    return this.userIsAdmin() || loggedUserId === userId;
  }

  enableFormEdit(): void {
    this.userForm.enable();
    this.userForm.get('password')?.clearValidators();
    this.userForm.get('password')?.updateValueAndValidity();
    this.formIsBeingEdited = true;
  }

  canEditProfilePicture(): boolean {
    const loggedUserId: string = this.loggedUserService.getUserId();
    const userId: string = this.userForm.get("id")?.value;

    return this.userForm.enabled && loggedUserId === userId;
  }

  getFormTitle(): string {
    return this.userForm.get("role")?.value.title === RolesEnum.CUSTOMER ? "Cliente" : "Técnico";
  }

  return(): void {
    this.location.back();
  }
}
