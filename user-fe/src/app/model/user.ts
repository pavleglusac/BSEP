export interface User {
    id: string;
    name: string;
    email: string;
    imageUrl: string;
    role: string;
  }
  
  enum Role {
    Tenant = 'ROLE_TENANT',
    Landlord = 'ROLE_LANDLORD',
  }
  
  