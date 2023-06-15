
import { Route } from '@angular/router';
import { CsrComponent } from '../components/csr/csr.component';
import { HomepageComponent } from '../components/homepage/homepage.component';
import { RealEstatesComponent } from '../components/user/realestates/realestates.component';
import { AuthGuard } from '../auth.guard';
import { MessagesComponent } from '../components/user/messages/messages.component';
import { ReportsComponent } from '../components/user/reports/reports.component';
import { ThreatsComponent } from '../components/threats/threats.component';

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
      {
        path: 'messages',
        component: MessagesComponent,
      },
      {
        path: 'reports',
        component: ReportsComponent,
      },
      {
        path: 'threats',
        component: ThreatsComponent,
      },
    ],
  },
];
