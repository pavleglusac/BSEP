import { CsrStateType } from '../types';
import { CsrAction, CsrActionType } from './csr.actions';

const initialState: CsrStateType = {
  csr: {
    id: '',
    commonName: '',
    surname: '',
    givenName: '',
    organization: '',
    organizationalUnit: '',
    state: '',
    country: '',
    email: '',
    creationDate: '',
    publicKey: '',
  },
};

export const CsrReducer = (
  state: CsrStateType = initialState,
  action: CsrAction
) => {
  switch (action.type) {
    case CsrActionType.ADD:
      return {
        ...state,
        csr: action.payload,
      };
    default: {
      return state;
    }
  }
};
