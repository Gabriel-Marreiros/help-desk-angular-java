import { UserModel } from "./user.model";

export interface CustomerModel extends UserModel {
  cnpj: string
}
