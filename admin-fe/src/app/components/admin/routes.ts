import { Route } from '@angular/router';
import { AdminComponent } from './admin.component';
import { CertificateComponent } from './certificate/certificate.component';
import { CertificatesComponent } from './certificates/certificates.component';
import { CreateCertificateComponent } from './create-certificate/create-certificate.component';

export const ADMIN_ROUTES: Route[] = [
  {
    path: '',
    component: AdminComponent,
    children: [
      {
        path: 'create-certificate',
        component: CreateCertificateComponent
      },
      {
        path: 'certificates',
        component: CertificatesComponent
      }
    ]
  },
  
  // ...
];
