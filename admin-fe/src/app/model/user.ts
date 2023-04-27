export interface User {
  id: string;
  name: string;
  email: string;
  emailVerified: boolean;
  imageUrl: string;
  role: string;
  locked: boolean;
}
export class RegisterUser {
  name: string = '';
  email: string = '';
  password: string = '';
  confirmPassword?: string = '';
  role: Role = Role.Tenant;
}

enum Role {
  Tenant = 'ROLE_TENANT',
  Landlord = 'ROLE_LANDLORD',
}
