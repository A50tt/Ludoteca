import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Category } from './model/Category';
import { CATEGORY_DATA } from './model/mock-categories';
import { StatusResponse } from '../core/model/StatusResponse';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http: HttpClient) { }

  private baseUrl = environment.baseUrl + '/category';

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.baseUrl);
  }

  saveCategory(category: Category): Observable<StatusResponse> {
    const { id } = category;
    const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;
    return this.http.put<StatusResponse>(url, category);
  }

  deleteCategory(idCategory : number | undefined): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${idCategory}`);
  }
}