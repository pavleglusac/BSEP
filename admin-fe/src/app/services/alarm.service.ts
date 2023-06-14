import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AlarmRule } from '../model/alarmRule';
import { Alarm } from '../model/alarm';

@Injectable({
  providedIn: 'root'
})
export class AlarmService {

  constructor(private http: HttpClient) {}

  addAlarmRule(
    alarmRule: AlarmRule,
    successCb: () => void,
    errorCb: (error: any) => void
  ) {
    this.http.post(`api/myhouse/alarm/rule`, alarmRule).subscribe({
      next(res: any) {
        successCb();
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  getAlarmRules(
    successCb: (alarmRules: AlarmRule[]) => void,
    errorCb: (error: any) => void
  ) {
    this.http.get(`api/myhouse/alarm/rule`).subscribe({
      next(res: any) {
        successCb(res);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  deleteAlarmRule(ruleName: string, successCb: () => void, errorCb: (error: any) => void) {
    this.http.delete(`api/myhouse/alarm/rule?ruleName=${ruleName}`).subscribe({
      next(value: any) {
        successCb();
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  getAlarms(
    page: number,
    successCb: (alarm: any) => void,
    errorCb: (error: any) => void
  ) {
    this.http.get(`api/myhouse/alarm?page=${page}&amount=20`).subscribe({
      next(res: any) {
        successCb(res);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

}
