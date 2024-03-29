import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environment/environment';
import {
  LoggedUserAction,
  LoggedUserActionType,
} from '../../shared/store/logged-user-slice/logged-user.actions';
import { Store } from '@ngrx/store';
import { StoreType } from '../../shared/store/types';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private store: Store<StoreType>) { 
    this.store.select('loggedUser').subscribe((res) => {
      console.log(res);
    });
  }

  getToken(): string | null {
    const token = sessionStorage.getItem(environment.tokenName);
    if (!token) return null;
    return token;
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
      .post('api/auth/login', { email, password, loginToken: code }, {withCredentials: true})
      .subscribe({
        next(value: any) {
          sessionStorage.setItem(environment.tokenName, value.accessToken);
          that.store.dispatch(new LoggedUserAction(LoggedUserActionType.LOGIN));
          that.http.get('api/auth/privileged', {
            headers: new HttpHeaders({'Accept':'text/plain'}),
            'responseType': 'text' as 'json',
            withCredentials: true
          })
            .subscribe({
              next(value: any) {
                successCb();
              },
              error(err) {
                console.log(err);
                sessionStorage.removeItem(environment.tokenName);
                that.store.dispatch(new LoggedUserAction(LoggedUserActionType.LOGOUT));
                errorCb({ message: 'Incorrect credentials.' });
              },
            });
        },
        error(err) {
          console.log(err);
          errorCb(err.error);
        },
      });
  };
}
