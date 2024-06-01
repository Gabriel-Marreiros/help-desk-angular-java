import { Injectable } from '@angular/core';
import * as ExcelJS from 'exceljs';
import { TicketModel } from 'src/app/typings/models/ticket.model';

@Injectable({
  providedIn: 'root'
})
export class ExcelService {

  constructor() { }

  async exportTicketsToExcel(tickets: Array<TicketModel>): Promise<Blob> {
    const workbook = new ExcelJS.Workbook();

    workbook.creator = 'Help Desk Angular & Java';
    workbook.created = new Date();
    workbook.description = "Todos os chamados.";

    const worksheet = workbook.addWorksheet('Todos os Chamados');

    worksheet.columns = [
      {header: "Código", key: "code"},
      {header: "Status", key: "ticketStatus", width: 15},
      {header: "Título", key: "title", width: 60},
      {header: "Cliente", key: "customerName", width: 30},
      {header: "Técnico", key: "technicalName", width: 30},
      {header: "Data de Abertura", key: "openingDate", width: 20},
      {header: "Data de Fechamento", key: "closedDate", width: 20},
      {header: "Prioridade", key: "priorityTitle", width: 15},
      {header: "Descrição", key: "description", width: 100}
    ]

    const data = tickets.map((ticket) => {
      return {
        ...ticket,
        openingDate: new Date(ticket.openingDate),
        closedDate: ticket.closedDate ? new Date(ticket.closedDate) : "",
        customerName: ticket.customer.name,
        technicalName: ticket.technical.name,
        priorityTitle: ticket.priority.title
      };
    })

    worksheet.addRows(data);

    const headers = worksheet.getRow(1)
    headers.font = {bold: true};
    headers.alignment = { vertical: 'middle', horizontal: 'center' };
    headers.border = {top: { style: 'thin' }, left: { style: 'thin' }, bottom: { style: 'thin' }, right: { style: 'thin' }}

    const buffer = await workbook.xlsx.writeBuffer();

    const file = new Blob([buffer], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'})

    return file;
  }
}
