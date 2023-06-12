export interface LogRule {
  id?: string;
  name: string;
  alarmText: string;
  num: number;
  operatorNum: string;
  usernames: string[];
  logType?: string;
  actionRegex?: string;
  detailsRegex?: string;
  ipAddressRegex?: string;
  window?: string;
}
