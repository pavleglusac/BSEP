import { Action } from '@ngrx/store';

export enum AlarmActionType {
  RESET_UNREAD_ALARMS = 'RESET_UNREAD_ALARMS',
  RESET_UNREAD_LOG_ALARMS = 'RESET_UNREAD_LOG_ALARMS',
  ADD_UNREAD_ALARMS = 'ADD_UNREAD_ALARMS',
  ADD_UNREAD_LOG_ALARMS = 'ADD_UNREAD_LOG_ALARMS', 
}

export class AlarmAction implements Action {
  constructor(readonly type: string, public payload?: number | any ) {}
}
