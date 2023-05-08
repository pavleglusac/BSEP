import { Action } from '@ngrx/store';
import { User } from 'src/app/model/user';

export enum LoggedUserActionType {
  LOGIN = 'LOGIN',
  LOGOUT = 'LOGOUT',
  SET_USER = 'SET_USER',
}

export class LoggedUserAction implements Action {
  constructor(readonly type: string, public payload?: User) {}
}
