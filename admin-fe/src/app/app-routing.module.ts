import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './core/authentication/auth.guard';
import { PageNotFoundComponent } from './components/pagenotfound/page-not-found.component';

const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'admin', loadChildren: () => import('./components/admin/routes').then(mod => mod.ADMIN_ROUTES), canActivate: [AuthGuard]},
  { path: '**', pathMatch: 'full', 
  component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
