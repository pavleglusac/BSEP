import { User } from "./user";

export class RealEstate {
    constructor(
      public id: string,
      public name: string,
      public address: string,
      public landlord: string,
      public tenants: User[],
      public devices?: Device[]
    ) {}
  }

  export class Device {
    constructor(
      public type: DeviceType,
      public name: string,
      public refreshRate: number,
      public filterRegex: string,
      public filePath?: string,
      public id?: string
    ) {}
  }
  
  export enum DeviceType {
    THERMOMETER = "THERMOMETER",
    MOTION_DETECTOR = "MOTION DETECTOR",
    LOCK = "LOCK",
    LAMP = "LAMP",
    GATE = "GATE",
  }
  

  export class Message {
    id: string = "";
    type: string = "";
    text: string = "";
    value: number = 0;
    timestamp: Date = new Date();
    deviceId: string = "";
  }