import { RolesEnum } from "../enums/roles.enum";

export interface UserModel {
  id: string
  email: string
  password: string
  role: RolesEnum
}
