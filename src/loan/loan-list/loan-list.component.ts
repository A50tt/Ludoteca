import { Loan } from '../model/Loan';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { LoanService } from '../loan.service';
import { Pageable } from '../../core/model/page/Pageable';
import { LoanEditComponent } from '../loan-edit/loan-edit.component';
import { DialogConfirmationComponent } from '../../core/dialog-confirmation/dialog-confirmation.component';
import { DialogMessageComponent } from '../../core/dialog-message/dialog-message.component';
import { DialogMessageService } from '../../core/dialog-message/dialog-message-service';
import { AlertService } from '../../core/alerts';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Client } from '../../client/model/Client';
import { Game } from '../../game/model/Game';
import { GameService } from '../../game/game.service';
import { ClientService } from '../../client/client.service';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule, provideNativeDateAdapter } from '@angular/material/core';
import { MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS } from '@angular/material-moment-adapter';
import { MY_DP_FORMAT } from '../loan-edit/date-picker/date-picker';
import { LoanPage } from '../model/LoanPage';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-loan-list',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    CommonModule,
    MatPaginatorModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './loan-list.component.html',
  styleUrl: './loan-list.component.scss',
  providers: [
    provideNativeDateAdapter(),
    MatDatepickerModule,
    { provide: MAT_DATE_FORMATS, useValue: MY_DP_FORMAT },
    { provide: MY_DP_FORMAT, useValue: 'es-ES' }, // Spanish locale
    { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS] },
  ]
})
export class LoanListComponent {
  pageNumber: number = 0;
  pageSize: number = 5;
  totalElements: number = 0;

  dataSource = new MatTableDataSource<Loan>();
  displayedColumns: string[] = ['id', 'game', 'client', 'startDate', 'endDate', 'action'];

  loans!: Loan[];
  games!: Game[];
  clients!: Client[];
  filterGameTitle!: string | null;
  filterClientName!: string | null;
  filterDate!: Date | null;

  pageable: Pageable = {
    pageNumber: this.pageNumber,
    pageSize: this.pageSize,
    sort: [
      {
        property: 'id',
        direction: 'ASC',
      },
    ],
  };

  constructor(
    private loanService: LoanService,
    private dialog: MatDialog,
    private errDialogService: DialogMessageService,
    private alertService: AlertService,
    private gameService: GameService,
    private clientService: ClientService
  ) { }

  ngOnInit(): void {
    this.loadPage();
  }

  loadPage(event?: PageEvent) {
    if (event != null) {
      this.pageable.pageSize = event.pageSize;
      this.pageable.pageNumber = event.pageIndex;
    }

    if (this.filterGameTitle == null && this.filterClientName == null && this.filterDate == null) {
      this.loanService.getAllLoans(this.pageable).subscribe((data) => {
        this.dataSource.data = data.content;
        this.pageNumber = data.pageable.pageNumber;
        this.pageSize = data.pageable.pageSize;
        this.totalElements = data.totalElements;
      });
    } else {
      this.onSearch();
    }
  }

  createLoan() {
    const dialogRef = this.dialog.open(LoanEditComponent, {
      data: {},
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.ngOnInit();
    });
  }

  deleteLoan(loan: Loan) {
    const dialogRef = this.dialog.open(DialogConfirmationComponent, {
      data: {
        title: 'Eliminar préstamo',
        description:
          'Atención, si borra el préstamo se perderán sus datos.<br> ¿Desea eliminar el préstamo?',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.loanService.deleteLoan(loan.id).subscribe({
          next: (response) => {
            this.alertService.success(response.extendedMessage);
            this.ngOnInit();
          },
          error: (err) => {
            this.errDialogService.openMsgErrorDialog(err.error.message, err.error.extendedMessage);
          },
        });
      }
    });
  }

  async onSearch(): Promise<void> {
    const gameTitle = this.filterGameTitle != null ? this.filterGameTitle : undefined;;
    const clientName = this.filterClientName != null ? this.filterClientName : undefined;
    const date = this.filterDate != null ? this.filterDate : undefined;

    const loansPage = await firstValueFrom(this.loanService.getGameClientDateFilteredLoans(this.pageable, this.filterDate, gameTitle, clientName));
    this.loans = loansPage.content;

    console.log("------- NEXT -------");
    this.dataSource.data = loansPage.content;
    this.pageNumber = this.pageable.pageNumber;
    this.pageSize = this.pageable.pageSize;
    this.totalElements = loansPage.totalElements
  }

  onCleanFilter(): void {
    this.filterGameTitle = '';
    this.filterClientName = null;
    this.filterDate = null;
    this.onSearch();
  }
}
