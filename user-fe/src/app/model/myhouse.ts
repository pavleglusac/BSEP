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
      public deviceType: DeviceType,
      public deviceName: string,
      public refreshRate: string,
      public regex: string,
      public filePath?: string,
    ) {}
  }
  
  export enum DeviceType {
    THERMOMETER = "THERMOMETER",
    MOTION_DETECTOR = "MOTION DETECTOR",
    LOCK = "LOCK",
    LAMP = "LAMP",
    GATE = "GATE",
  }
  