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
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LoanService {

  constructor(
    private http: HttpClient
  ) { }

  private baseUrl = environment.baseUrl + '/loan';

  getGameClientDateFilteredLoans(pageable: Pageable, date: Date | null, gameTitle: Game | null, clientName: Client | null): Observable<LoanPage> {
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

  private composeFindUrl(date: Date | null, gameTitle: Game | null, clientName: Client | null): string {
    const params = new URLSearchParams();
    if (date) {
      params.set('date', DateUtils.formatDate(date));
    }
    if (gameTitle) {
      params.set('gameTitle', gameTitle.title);
    }
    if (clientName) {
      params.set('clientName', clientName.name);
    }
    const queryString = params.toString();
    return queryString ? `${this.baseUrl}?${queryString}` : this.baseUrl;
  }
}
