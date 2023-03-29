import { localStorageSync } from 'ngrx-store-localstorage';
import { ActionReducer, MetaReducer } from '@ngrx/store';
import { StoreType } from './shared/store/types';
import * as CryptoJS from 'crypto-js';
import { environment } from 'src/environment/environment';

function localStorageSyncReducer(
  reducer: ActionReducer<StoreType>
): ActionReducer<StoreType> {
  return localStorageSync({
    keys: [
      {
        loggedUser: {
          encrypt: (state) => CryptoJS.AES.encrypt(state, environment.key).toString(),
          decrypt: (state) =>
            CryptoJS.AES.decrypt(state, environment.key).toString(CryptoJS.enc.Utf8),
        },
      },
    ],
    rehydrate: true,
    storage: sessionStorage,
  })(reducer);
}

export const metaReducers: Array<MetaReducer<StoreType, any>> = [
  localStorageSyncReducer,
];
