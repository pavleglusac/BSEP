import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RegisterUser } from '../../../model/user';
import { FormsModule } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styles: [],
  standalone: true,
  imports: [CommonModule, FormsModule],
})
export class RegisterComponent {
  user = new RegisterUser();
  errors = new RegisterUser();

  constructor(
    private userService: UserService,
    private toastr: ToastrService
  ) {}
  onSubmit = () => {
    if (this.valid()) {
      this.userService.register(
        this.user,
        (message: string) => {
          this.toastr.success(message);
        },
        (err) => this.toastr.error(err.message)
      );
    }
  };

  valid = (): boolean => {
    let valid = true;
    if (!/^.+[@].+[\.].+$/.test(this.user.email)) {
      this.errors.email =
        'Invalid format of email. Please enter email in format: username@domain.com.';
      valid = false;
    } else this.errors.email = '';
    if (
      !/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+])[0-9a-zA-Z!@#$%^&*()_+@]{12,20}$/.test(
        this.user.password
      )
    ) {
      this.errors.password =
        'Invalid format of password. Password must be at least 12 characters long and contain at least one capital letter and one number.';
      valid = false;
    } else this.errors.password = '';
    if (this.user.password !== this.user.confirmPassword) {
      this.errors.confirmPassword =
        'The password and confirmation password you have entered do not match. Please double-check your password and try again.';
      valid = false;
    } else this.errors.confirmPassword = '';
    if (!/^[A-Za-z\s]+$/.test(this.user.name)) {
      this.errors.name = 'Name must contain all letters.';
      valid = false;
    } else this.errors.name = '';
    return valid;
  };
}
