import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor() { }

  login(email: string, password: string): Promise<boolean> {
    // TODO: Implement method
    return new Promise((resolve) => { resolve(true) });
  }
}
