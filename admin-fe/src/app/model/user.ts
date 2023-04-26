export class RegisterUser {
  name: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';
  role: Role = Role.HOMEOWNER;
}

enum Role {
  HOMEOWNER,
}
