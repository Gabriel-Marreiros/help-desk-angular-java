import { UserModel } from "./user.model";

export interface TicketCommentModel {
  id: string;
  comment: string;
  user: UserModel;
  ticket?: string;
  edited: boolean;
  createdAt: Date;
  updatedAt?: Date;
}
