import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, firstValueFrom } from 'rxjs';
import { CustomerService } from 'src/app/services/customer/customer.service';
import { ExcelService } from 'src/app/services/excel/excel.service';
import { PriorityService } from 'src/app/services/priority/priority.service';
import { TechnicalService } from 'src/app/services/technical/technical.service';
import { TicketsService } from 'src/app/services/tickets/tickets.service';
import { LoadingModalComponent } from 'src/app/shared/components/loading-modal/loading-modal.component';
import { PriorityEnum } from 'src/app/typings/enums/priority.enum';
import { TicketStatusEnum } from 'src/app/typings/enums/ticket-status-enum';
import { IAllTicketsStatusSummary } from 'src/app/typings/interfaces/all-tickets-status-summary';
import { CustomerModel } from 'src/app/typings/models/customer.model';
import { PriorityModel } from 'src/app/typings/models/priority.model';
import { TechnicalModel } from 'src/app/typings/models/technical.model';
import { TicketModel } from 'src/app/typings/models/ticket.model';
import { LoggedUserService } from './../../../../services/logged-user-details/logged-user-details.service';

@Component({
  selector: 'app-tickets-grid',
  templateUrl: './tickets-grid.component.html',
  styleUrls: ['./tickets-grid.component.scss']
})
export class TicketsGridComponent implements OnInit {

  displayedColumns: Array<string> = ["code", "title", "customer", "technical", "openDate", "priority", "status"];
  matTableDataSource: MatTableDataSource<TicketModel> = new MatTableDataSource();
  tableData$: BehaviorSubject<Array<TicketModel>> = this.matTableDataSource.connect();
  ticketsStatusSummary!: IAllTicketsStatusSummary;
  ticketStatusEnum: typeof TicketStatusEnum = TicketStatusEnum;

  @ViewChild(MatPaginator, {static: true})
  paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true})
  sort!: MatSort;

  pageSizeOptions: Array<number> = [5, 10, 15];
  initialPageSize: number = 10;
  ticketsTotalLength!: number;
  currentPage: number = 0;

  technicalOptions!: Array<TechnicalModel>;
  customerOptions!: Array<CustomerModel>;
  priorityOptions!: Array<PriorityModel>;

  customerFilter!: string;
  technicalFilter!: string;
  statusFilter!: TicketStatusEnum;
  priorityFilter!: string;
  searchFilter!: string;

  constructor(
    protected loggedUserService: LoggedUserService,
    private ticketsService: TicketsService,
    private technicalService: TechnicalService,
    private customerService: CustomerService,
    private priorityService: PriorityService,
    private currentRoute: ActivatedRoute,
    private excelService: ExcelService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.getTechnicalsOptions();
    this.getCustomerOptions();
    this.getPriorityOptions();

    this.paginator.pageSize ??= this.initialPageSize;

    const firstPage: PageEvent = {
      pageIndex: 0,
      pageSize: this.paginator.pageSize,
      length: this.ticketsTotalLength
    }

    this.currentRoute.queryParams.subscribe(({page, technical, customer, status, priority, search}) => {
      this.currentPage = page;
      this.technicalFilter = technical;
      this.customerFilter = customer;
      this.statusFilter = status;
      this.priorityFilter = priority;
      this.searchFilter = search;

      this.getTicketsPaginated(firstPage);
    })

  }

  openTicketDetails(row: TicketModel){
    this.router.navigate(['detalhes', row.id], {
      relativeTo: this.currentRoute
    });
  }

  defineStatusColor(status: TicketStatusEnum): string {
    switch(status){
      case TicketStatusEnum.NEW_TICKET: return "text-bg-secondary";
      case TicketStatusEnum.PENDING: return "text-bg-warning";
      case TicketStatusEnum.IN_PROGRESS: return "text-bg-primary";
      case TicketStatusEnum.RESOLVED: return "text-bg-success";
      case TicketStatusEnum.CANCELED: return "text-bg-danger";
    }
  }

  definePriorityColor(priority: PriorityEnum): string {
    switch(priority){
      case PriorityEnum.LOW: return "text-bg-secondary";
      case PriorityEnum.MEDIUM: return "text-bg-warning";
      case PriorityEnum.HIGH: return "text-bg-danger";
    }
  }

  handleSearch(): void {
    this.router.navigate([], {queryParams: {search: this.searchFilter}, queryParamsHandling: 'merge'})
  }

  getTicketsPaginated(page: PageEvent): void {
    const loadingModalRef = this.dialog.open(LoadingModalComponent);

    const queryParams: {page: number, size: number, technical?: string, customer?: string, status?: string, priority?: string, search?: string} = {
      page: page.pageIndex,
      size: page.pageSize,
    }

    this.technicalFilter && (queryParams.technical = this.technicalFilter);
    this.customerFilter && (queryParams.customer = this.customerFilter);
    this.statusFilter && (queryParams.status = this.statusFilter);
    this.priorityFilter && (queryParams.priority = this.priorityFilter);
    this.searchFilter && (queryParams.search = this.searchFilter);

    this.ticketsService.getTicketsPaginated(queryParams).subscribe({
      next: (response) => {
        this.tableData$.next(response.content);
        this.ticketsTotalLength = response.totalElements;

        loadingModalRef.close();
      },

      error: (error) => {
        console.error(error);
        loadingModalRef.close();
      }
    });
  }

  getTechnicalsOptions(): void {
    this.technicalService.getAllTechnicals().subscribe({
      next: (response) => {
        this.technicalOptions = response.body!;
      },

      error: (error) => {
        console.error(error);
      }
    })
  }

  getCustomerOptions(): void {
    this.customerService.getAllCustomers().subscribe({
      next: (response) => {
        this.customerOptions = response.body!;
      },

      error: (error) => {
        console.error(error);
      }
    })
  }

  getPriorityOptions(): void {
    this.priorityService.getAllPriorities().subscribe({
      next: (response) => {
        this.priorityOptions = response.body!;
      },

      error: (error) => {
        console.error(error);
      }
    })
  }

  handleSelectFilter(filter: Record<string, string>): void {
    this.router.navigate(["/dashboard/chamados"], {
      queryParams: filter,
      queryParamsHandling: 'merge'
    });
  }

  handleClearSearchFilter(): void {
    this.searchFilter = '';
    this.router.navigate(["/dashboard/chamados"], {
      queryParams: {search: null},
      queryParamsHandling: 'merge'
    });
  }

  showClearSearchFilterButton(): boolean {
    let hasSearchParams: boolean = false;

    this.currentRoute.queryParams.subscribe((params) => {
      hasSearchParams = params["search"];
    });

    return hasSearchParams;
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
