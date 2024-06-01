import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, firstValueFrom } from 'rxjs';
import { ExcelService } from 'src/app/services/excel/excel.service';
import { TicketsService } from 'src/app/services/tickets/tickets.service';
import { LoadingModalComponent } from 'src/app/shared/loading-modal/loading-modal.component';
import { PriorityEnum } from 'src/app/typings/enums/priority.enum';
import { TicketStatusEnum } from 'src/app/typings/enums/ticket-status-enum';
import { IAllTicketsStatusSummary } from 'src/app/typings/interfaces/all-tickets-status-summary';
import { TicketModel } from 'src/app/typings/models/ticket.model';

@Component({
  selector: 'app-tickets-grid',
  templateUrl: './tickets-grid.component.html',
  styleUrls: ['./tickets-grid.component.scss']
})
export class TicketsGridComponent implements OnInit {

  displayedColumns: Array<string> = ["code", "title", "customer", "technical", "openDate", "priority", "status"];
  matTableDataSource: MatTableDataSource<TicketModel> = new MatTableDataSource();
  $tableData: BehaviorSubject<Array<TicketModel>> = this.matTableDataSource.connect();
  ticketsStatusSummary!: IAllTicketsStatusSummary;
  ticketStatusEnum: typeof TicketStatusEnum = TicketStatusEnum;

  @ViewChild(MatPaginator, {static: true})
  paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true})
  sort!: MatSort;

  pageSizeOptions: Array<number> = [5, 10, 15];
  initialPageSize: number = 5;
  ticketsTotalLength!: number;
  statusFilter: TicketStatusEnum | "todos" = "todos";
  searchFilter!: string;

  constructor(
    private ticketsService: TicketsService,
    private currentRoute: ActivatedRoute,
    private excelService: ExcelService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.getAllTicketsStatusSummary();

    this.currentRoute.queryParams.subscribe(({status, search}) => {

      this.paginator.pageSize ??= this.initialPageSize;

      if((!status && !search) || (status && search)){
        this.searchFilter = "";
        this.router.navigate([], {queryParams: {status: this.statusFilter}});
      }

      if(status){
        this.searchFilter = "";
        this.statusFilter = status;
        this.getTicketsByStatusPaginated();
      }

      if(search){
        this.searchFilter = search;
        this.getTicketsBySearchTermPaginated()
      }

    })

  }

  getAllTicketsStatusSummary(){
    this.ticketsService.getAllTicketsStatusSummary().subscribe({
      next: (response) => {
        this.ticketsStatusSummary = response.body!;
      }
    })
  }

  openTicketDetails(row: TicketModel){
    this.router.navigate(['formulario', row.id], {
      relativeTo: this.currentRoute
    });
  }

  defineStatusColor(status: TicketStatusEnum): string {
    switch(status){
      case TicketStatusEnum.IN_PROGRESS: return "text-bg-warning";
      case TicketStatusEnum.PENDING: return "text-bg-danger";
      case TicketStatusEnum.RESOLVED: return "text-bg-success";
    }
  }

  definePriorityColor(priority: PriorityEnum): string {
    switch(priority){
      case PriorityEnum.LOW: return "text-bg-secondary";
      case PriorityEnum.MEDIUM: return "text-bg-warning";
      case PriorityEnum.HIGH: return "text-bg-danger";
    }
  }

  getTicketsByStatusPaginated(page: PageEvent = {pageIndex: 0, pageSize: this.paginator.pageSize, length: this.ticketsTotalLength}){
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.ticketsService.getTicketsByStatusPaginated(page.pageIndex, page.pageSize, this.statusFilter).subscribe({
      next: (response) => {
        this.$tableData.next(response.content);
        this.ticketsTotalLength = response.totalElements;

        loadingModalRef.close();
      },

      error: (error) => {
        console.error(error);

        loadingModalRef.close();
      }
    });
  }

  getTicketsBySearchTermPaginated(page: PageEvent = {pageIndex: 0, pageSize: this.paginator.pageSize, length}): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    this.ticketsService.getTicketsBySearchTermPaginated(page.pageIndex, page.pageSize, this.searchFilter).subscribe({
      next: (response) => {
        this.$tableData.next(response.content);
        this.ticketsTotalLength = response.totalElements;

        loadingModalRef.close();
      },

      error: (error) => {
        console.error(error);

        loadingModalRef.close();
      }
    });
  }

  handleSearch(): void {
    this.router.navigate([], {queryParams: {search: this.searchFilter}})
  }

  handlePageChange(page: PageEvent): void {
    if(this.searchFilter){
      this.getTicketsBySearchTermPaginated(page);
    }
    else{
      this.getTicketsByStatusPaginated(page)
    }
  }

  async exportAllTicketsToExcel(): Promise<void> {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    const tickets = await firstValueFrom(this.ticketsService.getAllTickets());

    const file = await this.excelService.exportTicketsToExcel(tickets.body!);

    const url: string = URL.createObjectURL(file);
    const a = document.createElement('a');
    a.href = url;
    a.download = "Help Desk - Todos os Chamados";
    a.click();

    URL.revokeObjectURL(url);
    a.remove();

    loadingModalRef.close();
  }

}
