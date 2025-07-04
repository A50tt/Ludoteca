import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { Author } from './model/Author';
import { AuthorPage } from './model/AuthorPage';
import { HttpClient } from '@angular/common/http';
import { StatusResponse } from '../core/model/StatusResponse';
import { environment } from '../environments/environment';

@Injectable({
    providedIn: 'root',
})
export class AuthorService {
    constructor(private http: HttpClient) { }

    private baseUrl = environment.baseUrl + '/author';

    getAuthors(pageable: Pageable): Observable<AuthorPage> {
        return this.http.post<AuthorPage>(this.baseUrl, { pageable: pageable });
    }

    saveAuthor(author: Author): Observable<StatusResponse> {
        const { id } = author;
        const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;
        return this.http.put<StatusResponse>(url, author);
    }

    deleteAuthor(idAuthor: number): Observable<StatusResponse> {
        return this.http.delete<StatusResponse>(`${this.baseUrl}/${idAuthor}`);
    }

    getAllAuthors(): Observable<Author[]> {
        return this.http.get<Author[]>(this.baseUrl);
    }
}