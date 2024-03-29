import { Tenant } from "./user";

export class RealEstate {
  constructor(
    public id: string,
    public name: string,
    public address: string,
    public landlord: string,
    public tenants: Tenant[],
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
  MOTION_DETECTOR = "MOTION_DETECTOR",
  LOCK = "LOCK",    
  LAMP = "LAMP",
  GATE = "GATE",
}
