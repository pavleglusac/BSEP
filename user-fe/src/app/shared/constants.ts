
import { Route } from '@angular/router';
import { CsrComponent } from '../components/csr/csr.component';
import { HomepageComponent } from '../components/homepage/homepage.component';
import { RealEstatesComponent } from '../components/user/realestates/realestates.component';
import { AuthGuard } from '../auth.guard';
import { MyprofileComponent } from '../components/user/myprofile/myprofile.component';

export const tokenName = 'access_token';

export const baseUrl = 'https://localhost:8080';

export const key = 'aaaa';

export const ROUTES: Route[] = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: HomepageComponent,
    children: [
      {
        path: '',
        component: RealEstatesComponent,        
      },
      {
        path: 'csr',
        component: CsrComponent,
      },
    ],
  },
];
