import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faChevronLeft, IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from 'src/app/core/authentication/auth.service';
import { ToastrService } from 'ngx-toastr';


@Component({
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FontAwesomeModule],
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  faChevronLeft: IconDefinition = faChevronLeft;
  errorMessage: string = '';

  showPasswordChangeResponseModal: boolean = false;
  passwordChangeResponseModalTitle: string = '';
  passwordChangeResponseModalMessage: string = '';

  loginForm = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.pattern(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/)
    ]),
    password: new FormControl('', [
      Validators.required
    ]),
    code: new FormControl('', [
      Validators.required
    ]),
  });

  constructor(private authenticationService: AuthService, private toastr: ToastrService) { 
    document.getElementById('login-email')?.focus();
  }

  async onSubmit() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    };
    await this.authenticationService.login(
      this.email?.value!,
      this.password?.value!,
      this.code?.value!,
      () => {
        this.toastr.success('Login successful');
        window.location.href = '/admin/requests';
      },
      (error: any) => {
        this.toastr.error(error.message, 'Login failed');
        this.errorMessage = 'Incorrect credentials.';
      });
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }
  
  get code() {
    return this.loginForm.get('code');
  }
}
