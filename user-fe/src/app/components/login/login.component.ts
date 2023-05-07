import { Component } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { StoreType } from 'src/app/shared/store/types';
import {
  LoggedUserAction,
  LoggedUserActionType,
} from 'src/app/shared/store/logged-user-slice/logged-user.actions';
import { tokenName } from 'src/app/shared/constants';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule],
})
export class LoginComponent {
  page: number = 1;

  email: string = '';
  password: string = '';
  code: string = '';

  emailError: string = '';
  passwordError: string = '';
  codeError: string = '';

  constructor(
    private authService: AuthService,
    private toastr: ToastrService,
    private store: Store<StoreType>,
    private router: Router
  ) {}

  next = () => {
    this.page = 2;
  };

  previous = () => {
    this.page = 1;
  };

  onLogin = () => {
    if (this.valid()) {
      this.authService.login(
        this.email,
        this.password,
        this.code,
        (token) => {
          {
            sessionStorage.setItem(tokenName, token);
            this.store.dispatch(
              new LoggedUserAction(LoggedUserActionType.LOGIN)
            );
            this.toastr.success('Login successful');
            this.router.navigate(['/']);
          }
        },
        (error: any) => {
          this.toastr.error(error.message, 'Login failed');
        }
      );
    }
  };

  valid = () => {
    let valid = true;
    if (!this.code) {
      this.codeError = 'Verification code field is required.';
      valid = false;
    } else {
      this.codeError = '';
    }
    if (!/^.+[@].+[.].+$/.test(this.email)) {
      this.emailError =
        'Enter a valid email address in the format name@domain.com.';
      valid = false;
    } else {
      this.emailError = '';
    }
    if (!this.password) {
      this.passwordError = 'Password field is required.';
      valid = false;
    } else {
      this.passwordError = '';
    }
    return valid;
  };
}
