import { RolesEnum } from "../enums/roles.enum";
import { UserStatusEnum } from "../enums/user-status.enum";
import { RoleModel } from "./role.model";

export interface CustomerModel {
  customerId?: string
  userId?: string
  name: string
  cnpj: string
  phoneNumber: string
  email: string
  password?: string
  profilePicture: string
  userStatus: UserStatusEnum
  role: RoleModel
}
