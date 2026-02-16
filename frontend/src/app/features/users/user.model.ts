export type UserRole = 'admin' | 'transportista';

export interface User {
  id?: string;
  authUid?: string;
  userName: string;
  userEmail: string;
  userPassword?: string;
  userRole: UserRole;
}
