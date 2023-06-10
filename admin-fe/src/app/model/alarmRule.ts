export interface AlarmRule {
  id?: string;
  name: string;
  messageType: string;
  alarmText: string;
  num: number;
  operatorNum: string;
  window?: string;
  textRegex?: string;
  deviceType?: string;
  value?: number;
  operatorValue?: string;
}
