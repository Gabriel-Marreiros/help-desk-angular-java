import { TicketStatusEnum } from "../enums/ticket-status-enum";
import { CustomerModel } from "./customer.model";
import { PriorityModel } from "./priority.model";
import { TechnicalModel } from "./technical.model";

export interface TicketModel {
  id?: string
  code: string
  title: string
  description: string
  attachments?: Array<File>
  customer: CustomerModel
  technical: TechnicalModel
  openingDate: Date
  closedDate?: Date
  priority: PriorityModel
  ticketStatus: TicketStatusEnum
}
