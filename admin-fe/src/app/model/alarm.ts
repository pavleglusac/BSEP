import { Log } from "./log";
import { Device, RealEstate } from "./myHouse";

export interface Alarm {
  id: string;
  name: string;
  text: string;
  deviceType?: string;
  timestamp: string;
  device: Device;
  realEstate: RealEstate;
}

export class LogAlarm {
  id: string = '';
  name: string = '';
  text: string = '';
  timestamp: Date | null = null;
  logs: Log[] = [];
}