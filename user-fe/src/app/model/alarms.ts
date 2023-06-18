import { Device, Message, RealEstate } from "./myhouse";

export type Threat = Alarm | LogAlarm | MessageAlarm;

export class Alarm {
    id: string = '';
    name: string = '';
    text: string = '';
    deviceType?: string;
    timestamp: string = '';
    device: Device | null = null;
    realEstate: RealEstate | null = null;
  }
  
  export class LogAlarm {
    id: string = '';
    name: string = '';
    text: string = '';
    timestamp: Date | null = null;
    logs: Log[] = [];
  }

  export interface Log {
    id: string;
    timestamp: string;
    action: string;
    details: string;
    ipAddress: string;
    usernames: string[];
    type: string;
  }
  
export class MessageAlarm extends Message {
    realEstate: RealEstate | null = null;
    device: Device | null = null;
  }