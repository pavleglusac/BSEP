export class RealEstate {
  constructor(
    public name: string,
    public address: string,
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
