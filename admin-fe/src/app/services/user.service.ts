import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegisterUser } from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private httpClient: HttpClient) {}

  search(
    query: string,
    page: number,
    amount: number,
    roles: string[] | null = null,
    onlyLocked: boolean = false
  ): Observable<any> {
    return this.httpClient.get(`api/users?page=${page}&amount=${amount}\
    ${query ? '&query=' + query : ''}${
      roles ? '&roles=' + roles.join(',') : ''
    }${onlyLocked ? '&onlyLocked=' + onlyLocked : ''}`);
  }

  register(
    registerDto: RegisterUser,
    successCb: (message: string) => void,
    errorCb: (error: any) => void
  ) {
    this.httpClient.post(`api/auth/register`, registerDto).subscribe({
      next(value: any) {
        successCb(value.message);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  delete(
    id: string, 
    successCb: (message: string) => void,
    errorCb: (error: any) => void
  ) {
    this.httpClient.delete(`api/users/${id}`).subscribe({
      next(value: any) {
        successCb(value.message);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }
}
