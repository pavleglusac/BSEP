import { AlarmStateType } from '../types';
import { AlarmAction, AlarmActionType } from './threats.actions';
import { Alarm, LogAlarm, MessageAlarm } from 'src/app/model/alarms';


const initialState: AlarmStateType = {
  unreadMessagesAlarms: 0,
  unreadMessagesMessagesAlarms: 0,
  alarms: [],
  messagesAlarm: [],
  pageInfoAlarms: null,
  pageInfoMessagesAlarms: null
};

export const alarmReducer = (
  state: AlarmStateType = initialState,
  action: AlarmAction
) => {
  switch (action.type) {
    case AlarmActionType.SET_READ_MESSAGES_ALARMS:
      return {
        ...state,
        unreadMessagesAlarms: 0//state.unreadMessagesAlarms - state.alarms.length < 0 ? 0 : state.unreadMessagesAlarms - state.alarms.length,
      };
      case AlarmActionType.SET_READ_MESSAGES_MESSAGES_ALARMS:
        return {
          ...state,
          unreadMessagesMessagesAlarms: 0//state.unreadMessagesMessagesAlarms - state.messagesAlarm.length < 0 ? 0 : state.unreadMessagesMessagesAlarms - state.messagesAlarm.length,
        };
      case AlarmActionType.ADD_UNREAD_MESSAGES_ALARMS:
      return {
        ...state,
        unreadMessagesAlarms: state.unreadMessagesAlarms + action.payload as number,
      };
      case AlarmActionType.ADD_UNREAD_MESSAGES_MESSAGE_ALARMS:
        return {
          ...state,
          unreadMessagesMessagesAlarms: state.unreadMessagesMessagesAlarms + action.payload as number,
        };
  
    case AlarmActionType.ADD_ALARMS:
      return {
        ...state,
        alarms: [...action.payload.content as Alarm[]],
        pageInfoAlarms: action.payload
      };
    case AlarmActionType.ADD_MESSAGES_ALARMS:
      return {
        ...state,
        messagesAlarm: [ ...action.payload.content as MessageAlarm[]],
        pageInfoMessagesAlarms: action.payload
      };
    default: {
      return state;
    }
  }
};
