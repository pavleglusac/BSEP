import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  searchUsers(query: string, page: number, amount: number, roles: string[] | null = null, onlyLocked: boolean = false): Observable<any> {
    return this.httpClient.get(`api/users?page=${page}&amount=${amount}\
    ${query ? '&query=' + query : ''}${roles ? '&roles=' + roles.join(',') : ''}${onlyLocked ? '&onlyLocked=' + onlyLocked : ''}`);
  }
}
