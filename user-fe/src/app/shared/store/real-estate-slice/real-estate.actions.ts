import { Action } from '@ngrx/store';
import { RealEstate } from 'src/app/model/myhouse';
import { User } from 'src/app/model/user';

export enum RealEstateActionType {
  SET_REAL_ESTATES = 'SET_REAL_ESTATES',
}

export class RealEstateAction implements Action {
  constructor(readonly type: string, public payload: RealEstate[]) {}
}
