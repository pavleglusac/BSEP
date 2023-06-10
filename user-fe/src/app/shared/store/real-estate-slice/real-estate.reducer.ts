import { User } from 'src/app/model/user';

import { RealEstateAction, RealEstateActionType } from './real-estate.actions';
import { RealEstateStateType } from '../types';
import { RealEstate } from 'src/app/model/myhouse';

const initialState: RealEstateStateType = {
    realEstates: []
};

export const realEstateReducer = (
  state: RealEstateStateType = initialState,
  action: RealEstateAction
) => {
  switch (action.type) {
    case RealEstateActionType.SET_REAL_ESTATES:
      return {
        ...state,
        realEstates: <RealEstate[]>action.payload,
      };

    default: {
      return state;
    }
  }
};
