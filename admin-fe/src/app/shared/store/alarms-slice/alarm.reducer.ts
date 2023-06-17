import { AlarmStateType } from '../types';
import { AlarmAction, AlarmActionType } from './alarms.actions';

const initialState: AlarmStateType = {
  unreadAlarms: 0,
  unreadLogAlarms: 0,
};

export const alarmReducer = (
  state: AlarmStateType = initialState,
  action: AlarmAction
) => {
  switch (action.type) {
    case AlarmActionType.RESET_UNREAD_ALARMS:
      return {
        ...state,
        unreadAlarms: 0
      };
    case AlarmActionType.RESET_UNREAD_LOG_ALARMS:
      return {
        ...state,
        unreadLogAlarms: 0
      };
    case AlarmActionType.ADD_UNREAD_ALARMS:
      return {
        ...state,
        unreadAlarms: state.unreadAlarms + action.payload as number,
      };
    case AlarmActionType.ADD_UNREAD_LOG_ALARMS:
      return {
        ...state,
        unreadLogAlarms: state.unreadLogAlarms + action.payload as number,
      };
    default: {
      const s = retrieveState();
      if (s) {
        return JSON.parse(s);
      }
      return state;
    };
  }
}

const retrieveState = (): string | null => {
  return window.localStorage.getItem('unread');
}
