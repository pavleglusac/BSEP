import { Action } from '@ngrx/store';

export enum AlarmActionType {
  SET_READ_MESSAGES_ALARMS = 'SET_READ_MESSAGES_ALARMS',
  SET_READ_MESSAGES_MESSAGES_ALARMS = 'SET_READ_MESSAGES_MESSAGES_ALARMS',
  ADD_UNREAD_MESSAGES_ALARMS = 'ADD_UNREAD_MESSAGES_ALARMS',
  ADD_UNREAD_MESSAGES_MESSAGE_ALARMS = 'ADD_UNREAD_MESSAGES_MESSAGE_ALARMS', 
  ADD_ALARMS = 'ADD_ALARMS',
  ADD_MESSAGES_ALARMS = 'ADD_MESSAGES_ALARMS'
}

export class AlarmAction implements Action {
  constructor(readonly type: string, public payload?: number | any ) {}
}
