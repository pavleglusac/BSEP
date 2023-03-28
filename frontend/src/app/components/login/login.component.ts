import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faChevronLeft, IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { AuthenticationService } from 'src/app/core/authentication/authentication.service';


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
  });

  constructor(private authenticationService: AuthenticationService) { 
    document.getElementById('login-email')?.focus();
  }

  async onSubmit() {
    if (this.loginForm.invalid) return;
    const success = await this.authenticationService.login(this.email?.value!, this.password?.value!);
    console.log(success)
    if (success) {   
      // this.authenticationService.whoami();
      window.location.href = '/admin'
    }
    else {
      this.errorMessage = 'Incorrect credentials.';
    }
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  goHome(): void {
    window.location.href = '/';
  }

  closePasswordChangeResponseModal(): void {
    this.passwordChangeResponseModalTitle = '';
    this.passwordChangeResponseModalMessage = '';
    this.showPasswordChangeResponseModal = false;
  }
}
