import { LoggedUserStateType } from '../types';
import { LoggedUserAction, LoggedUserActionType } from './logged-user.actions';

const initialState: LoggedUserStateType = {
  logged: false,
};

export const loggedUserReducer = (
  state: LoggedUserStateType = initialState,
  action: LoggedUserAction
) => {
  switch (action.type) {
    case LoggedUserActionType.LOGIN:
      return {
        ...state,
        logged: true,
      };

    case LoggedUserActionType.LOGOUT:
      return {
        ...state,
        logged: false,
      };
    default: {
      return state;
    }
  }
};
