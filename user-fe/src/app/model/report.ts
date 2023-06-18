import { DeviceType } from "./myhouse";

export class Report {
    deviceType: DeviceType = DeviceType.THERMOMETER;
    deviceName: string = '';
    numberOfAlarmMessages: number = 0;
    numberOfInfoMessages: number = 0;
    numberOfMessages: number = 0;
    minValue: number = 0;
    maxValue: number = 0;
    averageValue: number = 0;
}