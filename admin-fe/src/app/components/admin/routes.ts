import { Route } from '@angular/router';
import { AdminComponent } from './admin.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { CreateCertificateComponent } from './create-certificate/create-certificate.component';
import { CsrsComponent } from './csrs/csrs.component';
import { UsersComponent } from './users/users.component';
import { RegisterComponent } from './register/register.component';
import { SettingsComponent } from './settings/settings.component';
import { AlarmRulesComponent } from './alarm-rules/alarm-rules.component';
import { AddAlarmRuleComponent } from './alarm-rules/add-alarm-rule/add-alarm-rule.component';
import { AddLogRuleComponent } from './log-rules/add-log-rule/add-log-rule.component';
import { LogRulesComponent } from './log-rules/log-rules.component';
import { AlarmsComponent } from './alarms/alarms.component';
import { LogsComponent } from './logs/logs.component';
import { LogAlarmsComponent } from './log-alarms/log-alarms.component';

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
        path: 'log-alarms',
        component: LogAlarmsComponent,
      },
      {
        path: 'alarm-rules',
        component: AlarmRulesComponent,
      },
      {
        path: 'alarm-rules/new',
        component: AddAlarmRuleComponent,
      },
      {
        path: 'logs',
        component: LogsComponent,
      },
      {
        path: 'log-rules',
        component: LogRulesComponent,
      },
      {
        path: 'log-rules/new',
        component: AddLogRuleComponent,
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
