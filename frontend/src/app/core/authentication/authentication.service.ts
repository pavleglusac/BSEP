import { Injectable } from '@angular/core';
import { axiosPrivate } from 'src/common/axiosPrivate';
import { axiosPublic } from "src/common/axiosPublic";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor() { }

  setJWT(data: { accessToken: string, expiresAt: string }): void {
    window.localStorage.setItem('jwt', JSON.stringify(data));
  }

  getToken(): string | null {
    const jwt = localStorage.getItem('jwt');
    if (!jwt) return null;
    return JSON.parse(jwt).accessToken;
  }

  async login(email: string, password: string): Promise<boolean> {
    const successfulLogin = await axiosPublic
      .post('/api/auth/login', {
        email,
        password,
      })
      .then((res) => {
        if (res.data) {
          this.setJWT(res.data);
          return true;
        } else {
          return false;
        }
      })
      .catch((err) => {
        console.log(err);
        return false;
      });
    return successfulLogin;
  }
}
