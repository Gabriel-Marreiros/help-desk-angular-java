import { UserStatusEnum } from "../enums/user-status.enum"

export interface ITechnicalWithTicketStatusCount {
  id: string
  name: string
  profilePicture: string
  userStatus: UserStatusEnum
  ticketsPending: number
  ticketsInProgress: number
  ticketsResolved: number
}
