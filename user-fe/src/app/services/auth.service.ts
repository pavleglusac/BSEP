import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  login = (email: string, password: string, code: string) => {
    this.http
      .post('api/auth/login', { email, password, loginToken: code })
      .pipe(
        tap((response) => {
          // Handle the success case here
          console.log('Request successful', response);
        }),
        catchError((error: HttpErrorResponse) => {
          // Handle the error case here
          console.error(error);
          return throwError(() => new Error(error.message));
        })
      );
  };
}
