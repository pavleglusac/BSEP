export const tokenName = 'access_token';

export const baseUrl = 'http://localhost:8080';

export const key = 'aaaa';

import { Route } from '@angular/router';
import { CsrComponent } from '../components/csr/csr.component';
import { HomepageComponent } from '../components/homepage/homepage.component';
import { RealEstatesComponent } from '../components/user/realestates/realestates.component';


export const ROUTES: Route[] = [
  {
    path: '',
    component: HomepageComponent,
    children: [
      {
        path: 'csr',
        component: CsrComponent,
      },
      {
        path: 'real-estates',
        component: RealEstatesComponent,
      },
    ],
  },
];
