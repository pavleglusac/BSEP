import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tokenName } from '../shared/constants';
import { Store } from '@ngrx/store';
import { StoreType } from '../shared/store/types';
import {
  LoggedUserAction,
  LoggedUserActionType,
} from '../shared/store/logged-user-slice/logged-user.actions';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private store: Store<StoreType>) {
    this.store.select('loggedUser').subscribe((res) => {
      console.log(res);
    });
  }

  login = (
    email: string,
    password: string,
    code: string,
    successCb: () => void,
    errorCb: (error: any) => void
  ) => {
    var that = this;
    this.http
      .post('api/auth/login', { email, password, loginToken: code })
      .subscribe({
        next(value: any) {
          sessionStorage.setItem(tokenName, value.accessToken);
          that.store.dispatch(new LoggedUserAction(LoggedUserActionType.LOGIN));
          successCb();
        },
        error(err) {
          console.log(err);
          errorCb(err.error);
        },
      });
  };
}
