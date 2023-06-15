import { AlarmStateType } from '../types';
import { AlarmAction, AlarmActionType } from './threats.actions';
import { Alarm, LogAlarm, MessageAlarm } from 'src/app/model/alarms';


const initialState: AlarmStateType = {
  unreadMessages: 0,
  alarms: [],
  logAlarms: [],
  messagesAlarm: []
};

export const alarmReducer = (
  state: AlarmStateType = initialState,
  action: AlarmAction
) => {
  switch (action.type) {
    case AlarmActionType.SET_UNREAD_MESSAGES:
      return {
        ...state,
        unreadMessages: action.payload as number,
      };

    case AlarmActionType.ADD_ALARMS:
      return {
        ...state,
        alarms: [...state.alarms, ...action.payload as Alarm[]],
        unreadMessages: state.unreadMessages + (action.payload as Alarm[]).length
      };
    case AlarmActionType.ADD_LOG_ALARMS:
      return {
        ...state,
        logAlarms: [...state.logAlarms, ...action.payload as LogAlarm[]],
        unreadMessages: state.unreadMessages + (action.payload as LogAlarm[]).length
      };
    case AlarmActionType.ADD_MESSAGES_ALARMS:
      return {
        ...state,
        messagesAlarm: [...state.messagesAlarm, ...action.payload as MessageAlarm[]],
        unreadMessages: state.unreadMessages + (action.payload as MessageAlarm[]).length
      };
      case AlarmActionType.MARK_AS_READ_ALARMS:
      return {
        ...state,
        alarms: state.alarms.map( alarm => {alarm.read = true; return alarm}),
        unreadMessages: state.unreadMessages - state.alarms.length
      };
    case AlarmActionType.MARK_AS_READ_LOG_ALARMS:
      return {
        ...state,
        logAlarms: state.logAlarms.map( logAlarm => {logAlarm.read = true; return logAlarm}),
        unreadMessages: state.unreadMessages - state.logAlarms.length
      };
    case AlarmActionType.MARK_AS_READ_MESSAGES_ALARMS:
      return {
        ...state,
        messagesAlarm: state.messagesAlarm.map(messageAlarm => {messageAlarm.read = true; return messageAlarm}),
        unreadMessages: state.unreadMessages - state.messagesAlarm.length
      };
    default: {
      return state;
    }
  }
};
