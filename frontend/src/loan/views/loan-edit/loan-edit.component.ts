import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { LoanService } from '../../loan.service';
import { Loan } from '../../model/Loan';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Client } from '../../../client/model/Client';
import { Game } from '../../../game/model/Game';
import { MatSelectModule } from '@angular/material/select';
import { CommonModule, NgFor } from '@angular/common';
import { ClientService } from '../../../client/client.service';
import { Observable } from 'rxjs';
import { GameService } from '../../../game/game.service';
import { DateAdapter, MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MY_DP_FORMAT } from './date-picker/date-picker';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { AlertService } from '../../../core/alerts';

import { MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS } from '@angular/material-moment-adapter';
import { DialogMessageService } from '../../../core/dialog-message/dialog-message-service';
import { DateUtils } from '../../../shared/date-utils';


@Component({
  selector: 'app-loan-edit',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    NgFor,
    CommonModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './loan-edit.component.html',
  styleUrl: './loan-edit.component.scss',
  providers: [
    provideNativeDateAdapter(),
    MatDatepickerModule,
    { provide: MAT_DATE_FORMATS, useValue: MY_DP_FORMAT },
    { provide: MY_DP_FORMAT, useValue: 'es-ES' }, // Spanish locale
    { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS] },
  ]
})

export class LoanEditComponent implements OnInit {
  static readonly MAX_DAYS_OF_LOAN = 14;
  dateMaxLengthError = false;

  loan!: Loan;
  registeredClients!: Observable<Client[]>
  registeredGames!: Observable<Game[]>

  constructor(
    public dialogRef: MatDialogRef<LoanEditComponent>,
    private loanService: LoanService,
    private clientService: ClientService,
    private gameService: GameService,
    private dateAdapter: DateAdapter<Date>,
    private alertService: AlertService,
    private errDialogService: DialogMessageService,
  ) {
    this.dateAdapter.setLocale('es-ES');
  }

  ngOnInit(): void {
    this.loan = new Loan();
    this.loan.game = new Game();
    this.loan.client = new Client();
    this.registeredClients = this.clientService.getClients();
    this.registeredGames = this.gameService.getGames();
  }

  onSave() {
    // Si startDate o endDate no están definidos por el usuario, fuerza el validation check y no envía al servidor.
    if (!this.loan.startDate || !this.loan.endDate) {
      const startDateInput = document.querySelector('[name="startDate"]') as HTMLElement;
      const endDateInput = document.querySelector('[name="endDate"]') as HTMLElement;

      startDateInput?.focus();
      startDateInput?.blur();

      endDateInput?.focus();
      endDateInput?.blur();
    } else {
      this.loanService.saveLoan(this.loan).subscribe({
        next: (result) => {
          this.alertService.success(result.extendedMessage);
          this.dialogRef.close();
        },
        error: (err) => {
          this.errDialogService.openMsgErrorDialog(err.error.message, err.error.extendedMessage);
        }
      });
    }
  }

  onClose() {
    this.dialogRef.close();
  }

  onStartDateSet() {
    // Se ha borrado startDate o endDate después de error? Desactiva error.
    if (!this.loan.startDate || !this.loan.endDate) {
      this.dateMaxLengthError = false;
      return;
    }
    
    // startDate mayor que endDate? Aumenta endDate a startDate.
    const startDate = (this.loan.startDate as any)?.toDate ? (this.loan.startDate as any).toDate() : new Date(this.loan.startDate);
    const endDate = (this.loan.endDate as any)?.toDate ? (this.loan.endDate as any).toDate() : new Date(this.loan.endDate);
    if (startDate > endDate) {
      this.loan.endDate = this.loan.startDate;
      this.dateMaxLengthError = false;
    } else {

      // Más de 14 días de préstamo?
      const daysDifference = DateUtils.getDaysDifference(this.loan.startDate, this.loan.endDate);
      if (daysDifference > LoanEditComponent.MAX_DAYS_OF_LOAN) {
        this.dateMaxLengthError = true;
      } else {
        this.dateMaxLengthError = false;
      }
    }
  }

  onEndDateSet() {
    // Se ha borrado startDate o endDate después de error? Desactiva error.
    if (!this.loan.startDate || !this.loan.endDate) {
      this.dateMaxLengthError = false;
      return;
    }

    // startDate mayor que endDate? Reduce startDate a endDate.
    if (this.loan.startDate > this.loan.endDate) {
      this.loan.startDate = this.loan.endDate;
      this.dateMaxLengthError = false;
      return;
    }
    
    // Más de 14 días de préstamo?
    const daysDifference = DateUtils.getDaysDifference(this.loan.startDate, this.loan.endDate);
    if (daysDifference > LoanEditComponent.MAX_DAYS_OF_LOAN) {
      this.dateMaxLengthError = true;
    } else {
      this.dateMaxLengthError = false;
    }
  }
}