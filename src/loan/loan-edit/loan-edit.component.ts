import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { LoanService } from '../loan.service';
import { Loan } from '../model/Loan';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Client } from '../../client/model/Client';
import { Game } from '../../game/model/Game';
import { MatSelectModule } from '@angular/material/select';
import { CommonModule, NgFor } from '@angular/common';
import { ClientService } from '../../client/client.service';
import { Observable } from 'rxjs';
import { GameService } from '../../game/game.service';
import { DateAdapter, MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MY_DP_FORMAT } from './date-picker/date-picker';
import { MAT_DATE_FORMATS } from '@angular/material/core';
import { AlertService } from '../../core/alerts';

import { MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS } from '@angular/material-moment-adapter';
import { DialogMessageComponent } from '../../core/dialog-message/dialog-message.component';
import { DialogMessageService } from '../../core/dialog-message/dialog-message-service';


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
}