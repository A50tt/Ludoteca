import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Game } from './model/Game';
import { HttpClient } from '@angular/common/http';
import { StatusResponse } from '../core/model/StatusResponse';
import { environment } from '../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class GameService {

    constructor(private http: HttpClient) { }

    private baseUrl = environment.baseUrl + '/game';

    getGames(title?: string, categoryId?: number): Observable<Game[]> {
        return this.http.get<Game[]>(this.composeFindUrl(title, categoryId));
    }

    saveGame(game: Game): Observable<StatusResponse> {
        if (game === undefined) {
            return of({
                message: 'Datos incompletos',
                extendedMessage: 'Por favor, introduzca todos los campos obligatorios.'
            });
        } else {
            const { id } = game;
            const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;
            return this.http.put<StatusResponse>(url, game);
        }
    }

    private composeFindUrl(title?: string, categoryId?: number): string {
        const params = new URLSearchParams();
        if (title) {
            params.set('title', title);
        }
        if (categoryId) {
            params.set('idCategory', categoryId.toString());
        }
        const queryString = params.toString();
        return queryString ? `${this.baseUrl}?${queryString}` : this.baseUrl;
    }

}