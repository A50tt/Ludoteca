import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Client } from './model/Client';
import { StatusResponse } from '../core/model/StatusResponse';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) { }

  private baseUrl = 'http://localhost:8080/client';

  getClients(name?: string): Observable<Client[]> {
    return this.http.get<Client[]>(this.composeFindUrl(name));
  }

  saveClient(client: Client): Observable<StatusResponse> {
    const {id, name} = client;
    const url = client.id ? `${this.baseUrl}/${client.id}` : this.baseUrl;
    return this.http.put<StatusResponse>(url, client);
  }

  deleteClient(id: Number): Observable<StatusResponse> {
    return this.http.delete<StatusResponse>(`${this.baseUrl}/${id}`);
  }

  private composeFindUrl(name?: string): string {
        const params = new URLSearchParams();
        if (name) {
            params.set('name', name);
        }
        const queryString = params.toString();
        return queryString ? `${this.baseUrl}?${queryString}` : this.baseUrl;
    }
}
