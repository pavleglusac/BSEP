import { Action } from '@ngrx/store';
import { Alarm, LogAlarm, MessageAlarm } from 'src/app/model/alarms';

export enum AlarmActionType {
  SET_UNREAD_MESSAGES = 'SET_UNREAD_MESSAGES',
  ADD_ALARMS = 'ADD_ALARMS',
  ADD_LOG_ALARMS = 'ADD_LOG_ALARMS',
  ADD_MESSAGES_ALARMS = 'ADD_MESSAGES_ALARMS',
  MARK_AS_READ_ALARMS = 'MARK_AS_READ_ALARMS',
  MARK_AS_READ_LOG_ALARMS = 'MARK_AS_READ_LOG_ALARMS',
  MARK_AS_READ_MESSAGES_ALARMS = 'MARK_AS_READ_MESSAGES_ALARMS',
}

export class AlarmAction implements Action {
  constructor(readonly type: string, public payload?: number | Alarm[] | LogAlarm[] | MessageAlarm[]) {}
}
