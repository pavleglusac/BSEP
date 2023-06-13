import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { PageNotFoundComponent } from './components/pagenotfound/page-not-found.component';

const routes: Routes = [
  {path: '', loadChildren: () => import('./shared/constants').then(mod => mod.ROUTES)},
  { path: 'login', component: LoginComponent },
  { path: '**', pathMatch: 'full', 
  component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}


