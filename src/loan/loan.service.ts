import { Injectable } from '@angular/core';
import { Pageable } from '../core/model/page/Pageable';
import { Observable, of, throwError } from 'rxjs';
import { LoanPage } from './model/LoanPage';
import { Loan } from './model/Loan';
import { HttpClient } from '@angular/common/http';
import { StatusResponse } from '../core/model/StatusResponse';
import { DateUtils } from '../shared/date-utils';
import { Client } from '../client/model/Client';
import { Game } from '../game/model/Game';
import { GameService } from '../game/game.service';
import { ClientService } from '../client/client.service';

@Injectable({
  providedIn: 'root'
})
export class LoanService {

  constructor(
    private http: HttpClient,
    private gameService: GameService,
    private clientService: ClientService
  ) { }

  private baseUrl = 'http://localhost:8080/loan';

  getGameClientDateFilteredLoans(pageable: Pageable, date: Date | null, gameTitle: string | null, clientName: string | null): Observable<LoanPage> {
    const url = this.composeFindUrl(date, gameTitle, clientName);
    const body = { pageable: pageable };
    return this.http.post<LoanPage>(url, body);
  }

  getAllLoans(pageable: Pageable): Observable<LoanPage> {
    return this.http.post<LoanPage>(this.baseUrl, { pageable: pageable });
  }

  saveLoan(loan: Loan): Observable<StatusResponse> {
    loan.startDate = DateUtils.formatDate(loan.startDate);
    loan.endDate = DateUtils.formatDate(loan.endDate);
    return this.http.put<StatusResponse>(this.baseUrl, loan);
  }

  deleteLoan(id: number): Observable<StatusResponse> {
    return this.http.delete<StatusResponse>(`${this.baseUrl}/${id}`);
  }

  private composeFindUrl(date: Date | null, gameTitle: string | null, clientName: string | null): string {
    const params = new URLSearchParams();
    if (date) {
      params.set('date', DateUtils.formatDate(date));
    }
    if (gameTitle) {
      params.set('gameTitle', gameTitle);
    }
    if (clientName) {
      params.set('clientName', clientName);
    }
    const queryString = params.toString();
    return queryString ? `${this.baseUrl}?${queryString}` : this.baseUrl;
  }
}
