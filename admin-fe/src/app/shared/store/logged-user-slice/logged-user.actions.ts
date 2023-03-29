import { Action } from '@ngrx/store';

export enum LoggedUserActionType {
  LOGIN = 'LOGIN',
  LOGOUT = 'LOGOUT',
}

export class LoggedUserAction implements Action {
  constructor(readonly type: string) {} //public payload?: User
}
