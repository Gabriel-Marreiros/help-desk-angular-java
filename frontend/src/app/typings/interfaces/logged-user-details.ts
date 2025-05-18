import { RolesEnum } from "../enums/roles.enum"

export interface ILoggedUser {
  iss: string
  sub: string
  role: RolesEnum
  roleId: string
  name: string
  id: string
  profilePicture: string
}
