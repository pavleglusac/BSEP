import { User } from "src/app/model/user";

export type LoggedUserStateType = {
  logged: boolean;
  user: User | null;
};

export type StoreType = {
  loggedUser: LoggedUserStateType;
};
