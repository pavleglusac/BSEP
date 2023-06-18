import { RealEstate } from "src/app/model/myhouse";
import { User } from "src/app/model/user";
import { Alarm, LogAlarm, MessageAlarm, Threat } from "src/app/model/alarms";

export type LoggedUserStateType = {
  logged: boolean;
  user: User | null;
};

export type RealEstateStateType = {
  realEstates: RealEstate[];
};

export type AlarmStateType = {
  unreadMessagesAlarms: number;
  unreadMessagesMessagesAlarms: number;
  alarms: Alarm[];
  pageInfoAlarms: any;
  messagesAlarm: MessageAlarm[];
  pageInfoMessagesAlarms: any;
}

export type StoreType = {
  loggedUser: LoggedUserStateType;
  realEstates: RealEstateStateType;
  threats: AlarmStateType;
};
