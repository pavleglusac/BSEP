import { Action } from '@ngrx/store';
import { Csr } from 'src/app/model/certificate';

export enum CsrActionType {
  ADD = 'ADD',
}

export class CsrAction implements Action {
  constructor(readonly type: string, public payload: Csr) {}
}
