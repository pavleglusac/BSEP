import { Csr } from 'src/app/model/certificate';
import { CertificateStateType } from './certificate-slice/certificate.reducer';

export type LoggedUserStateType = {
  logged: boolean;
};

export type CsrStateType = {
  csr: Csr;
};

export type StoreType = {
  loggedUser: LoggedUserStateType;
  certificate: CertificateStateType;
  csr: CsrStateType;
};
