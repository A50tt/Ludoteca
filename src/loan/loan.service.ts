import { Injectable } from '@angular/core';
import { Pageable } from '../core/model/page/Pageable';
import { LOAN_DATA } from './model/mock-loans';
import { Observable, of } from 'rxjs';
import { LoanPage } from './model/LoanPage';
import { Loan } from './model/Loan';

@Injectable({
  providedIn: 'root'
})
export class LoanService {

  constructor() { }

  getLoans(pageable : Pageable): Observable<LoanPage> {
    return of(LOAN_DATA)
  }

  saveLoan(loan: Loan) {
    return of(1);
  }

  deleteLoan(id: number) {
    return of(1);
  }
}
