import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tokenName } from '../shared/constants';
import { Store } from '@ngrx/store';
import { StoreType } from '../shared/store/types';
import {
  LoggedUserAction,
  LoggedUserActionType,
} from '../shared/store/logged-user-slice/logged-user.actions';
import { Router } from '@angular/router';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  login = (
    email: string,
    password: string,
    code: string,
    successCb: (token: string) => void,
    errorCb: (error: any) => void
  ) => {
    this.http
      .post('api/auth/login', { email, password, loginToken: code })
      .subscribe({
        next(value: any) {
          successCb(value.accessToken);
        },
        error(err) {
          errorCb(err.error);
        },
      });
  };

  getUser = (successCb: (user: User) => void, errorCb: (error: any) => void) => {
    this.http.get('api/auth/my-profile')
    .subscribe({
      next(value: any) {
        console.log(value);
        successCb(value);
      },
      error(err) {
        errorCb(err.error);
      },
    })
  }
}
