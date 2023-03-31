import { CertificateStateType } from "./certificate-slice/certificate.reducer";

export type LoggedUserStateType = {
  logged: boolean;
};

export type StoreType = {
  loggedUser: LoggedUserStateType;
  certificate: CertificateStateType;
};
