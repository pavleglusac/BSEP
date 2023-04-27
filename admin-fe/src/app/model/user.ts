export interface User {
  id: string;
  name: string;
  email: string;
  emailVerified: boolean;
  imageUrl: string;
  role: string;
  locked: boolean;
}