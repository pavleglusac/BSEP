export interface Log {
  id: string;
  timestamp: string;
  action: string;
  details: string;
  ipAddress: string;
  usernames: string[];
  type: string;
  read: boolean;
}
