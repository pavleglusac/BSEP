import { Csr } from 'src/app/model/certificate';
import { CertificateStateType } from './certificate-slice/certificate.reducer';

export type LoggedUserStateType = {
  logged: boolean;
};

export type CsrStateType = {
  csr: Csr;
};

export type AlarmStateType = {
  unreadAlarms: number;
  unreadLogAlarms: number;
}

export type StoreType = {
  alarms: AlarmStateType;
  loggedUser: LoggedUserStateType;
  certificate: CertificateStateType;
  csr: CsrStateType;
};
