import { UserStatusEnum } from "../enums/user-status.enum";
import { RoleModel } from "./role.model";

export interface UserModel {
  id: string
  name: string
  email: string
  password?: string
  phoneNumber: string
  role: RoleModel
  profilePicture?: string
  userStatus: UserStatusEnum
}
