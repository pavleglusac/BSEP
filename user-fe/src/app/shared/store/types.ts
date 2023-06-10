import { RealEstate } from "src/app/model/myhouse";
import { User } from "src/app/model/user";

export type LoggedUserStateType = {
  logged: boolean;
  user: User | null;
};

export type RealEstateStateType = {
  realEstates: RealEstate[];
};

export type StoreType = {
  loggedUser: LoggedUserStateType;
  realEstates: RealEstateStateType;
};
