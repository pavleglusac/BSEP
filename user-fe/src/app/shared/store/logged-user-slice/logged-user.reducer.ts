import { User } from 'src/app/model/user';
import { LoggedUserStateType } from '../types';
import { LoggedUserAction, LoggedUserActionType } from './logged-user.actions';

const initialState: LoggedUserStateType = {
  logged: false,
  user: null,
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
    case LoggedUserActionType.SET_USER:
      return {
        logged: true,
        user: action.payload as User,
      }
    default: {
      return state;
    }
  }
};
