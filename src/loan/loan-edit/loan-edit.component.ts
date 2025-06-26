import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
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
import { DateAdapter, MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { provideNativeDateAdapter } from '@angular/material/core';
import { MY_DP_FORMAT } from './date-picker/date-picker';
import { MAT_DATE_FORMATS } from '@angular/material/core';


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
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  templateUrl: './loan-edit.component.html',
  styleUrl: './loan-edit.component.scss',
  providers: [
    provideNativeDateAdapter(),
    MatDatepickerModule,
    { provide: MAT_DATE_FORMATS, useValue: MY_DP_FORMAT },
    { provide: MY_DP_FORMAT, useValue: 'es-ES' }, // Spanish locale
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
    private dateAdapter: DateAdapter<Date>
  ) {
    this.dateAdapter.setLocale('es-ES'); // Explicitly set the locale
    console.log('Locale set to es-ES');
  }

  ngOnInit(): void {
    this.loan = new Loan();
    this.loan.game = new Game();
    this.loan.client = new Client();
    this.registeredClients = this.clientService.getClients();
    this.registeredGames = this.gameService.getGames();
  }

  onSave() {
    this.loanService.saveLoan(this.loan).subscribe({
      next: (result) => {
        this.dialogRef.close();
      },
      error: (err) => {
        console.log("ERROR! MEEH", err)
      }
    });
  }

  onClose() {
    this.dialogRef.close();
  }
}