import { Route } from '@angular/router';
import { AdminComponent } from './admin.component';
import { CertificateComponent } from './certificate/certificate.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { CreateCertificateComponent } from './create-certificate/create-certificate.component';
import { CsrsComponent } from './csrs/csrs.component';
import { UsersComponent } from './users/users.component';
import { RegisterComponent } from './register/register.component';
import { SettingsComponent } from './settings/settings.component';
import { AlarmsComponent } from './alarms/alarms.component';
import { AddAlarmComponent } from './alarms/add-alarm/add-alarm.component';

export const ADMIN_ROUTES: Route[] = [
  {
    path: '',
    component: AdminComponent,
    children: [
      {
        path: 'requests',
        component: CsrsComponent,
      },
      {
        path: 'create-certificate',
        component: CreateCertificateComponent,
      },
      {
        path: 'certificates',
        component: CertificatesComponent,
      },
      {
        path: 'users',
        component: UsersComponent,
      },
      {
        path: 'alarms',
        component: AlarmsComponent,
      },
      {
        path: 'alarms/new',
        component: AddAlarmComponent,
      },
      {
        path: 'register',
        component: RegisterComponent,
      },
      {
        path: 'settings',
        component: SettingsComponent,
      },
      {
        path: 'settings/:email',
        component: SettingsComponent,
      },
    ],
  },

  // ...
];
