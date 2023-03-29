import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {path: '', loadComponent: () => import('./components/login/login.component').then(mod => mod.LoginComponent)},
  {path: 'admin', loadChildren: () => import('./components/admin/routes').then(mod => mod.ADMIN_ROUTES)},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
