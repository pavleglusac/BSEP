import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { tokenName } from './shared/constants';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {

  constructor(private router: Router) { }

  canActivate(): boolean {
    let token: string | null = window.sessionStorage.getItem(tokenName);
    console.log(token)
    if (!token) {
      this.router.navigate(['/login']);
      return false;
    }
    
    return true;
  }
}
