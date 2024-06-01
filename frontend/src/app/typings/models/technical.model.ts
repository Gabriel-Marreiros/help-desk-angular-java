import { RolesEnum } from "../enums/roles.enum";
import { UserStatusEnum } from "../enums/user-status.enum";
import { RoleModel } from "./role.model";

export interface TechnicalModel {
  technicalId: string
  userId: string
  name: string
  dateBirth: Date
  email: string
  phoneNumber: string
  password: string
  profilePicture?: string
  role: RoleModel
  userStatus: UserStatusEnum
}


