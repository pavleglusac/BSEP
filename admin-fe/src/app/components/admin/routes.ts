import { Route } from '@angular/router';
import { AdminComponent } from './admin.component';
import { CertificateComponent } from './certificate/certificate.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { CreateCertificateComponent } from './create-certificate/create-certificate.component';
import { CsrsComponent } from './csrs/csrs.component';
import { UsersComponent } from './users/users.component';
import { RegisterComponent } from './register/register.component';

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
      }
      {
        path: 'register',
        component: RegisterComponent,
      },
    ],
  },

  // ...
];
