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
  deviceId?: string;
  value?: number;
  operatorValue?: string;
}
