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
      public filePath: string,
      public durationFormat: string,
      public filterRegex: string
    ) {}
  }
  
  export enum DeviceType {
    CAMERA,
    ALARM,
    SMART_LOCK,
    SMART_LIGHT,
    SMART_GATE,
  }
  