import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Client } from './model/Client';
import { CLIENT_DATA } from './model/mock-clients';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) { }

  private baseUrl = 'http://localhost:8080/clients';

  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.baseUrl);
  }

  saveClient(client: Client): Observable<Client> {
    const {id, name} = client;
    const url = client.id ? `${this.baseUrl}/${client.id}` : this.baseUrl;
    console.log("🪼🪼",this.http.put<Client>(url, client))
    return this.http.put<Client>(url, client);
  }

  deleteClient(id: Number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
