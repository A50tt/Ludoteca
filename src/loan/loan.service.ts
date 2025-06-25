import { Injectable } from '@angular/core';
import { Pageable } from '../core/model/page/Pageable';
import { LOAN_DATA } from './model/mock-loans';
import { Observable, of, throwError } from 'rxjs';
import { LoanPage } from './model/LoanPage';
import { Loan } from './model/Loan';
import { HttpClient } from '@angular/common/http';
import { StatusResponse } from '../core/model/StatusResponse';

@Injectable({
  providedIn: 'root'
})
export class LoanService {

  constructor(private http: HttpClient) { }

  private baseUrl = 'http://localhost:8080/loan';

  getLoans(pageable: Pageable): Observable<LoanPage> {
    return this.http.post<LoanPage>(this.baseUrl, { pageable: pageable });
  }

  saveLoan(loan: Loan): Observable<Loan> {
    return this.http.put<Loan>(this.baseUrl, loan);
  }

  deleteLoan(id: number): Observable<StatusResponse> {
    return this.http.delete<StatusResponse>(`${this.baseUrl}/${id}`);
  }
}
