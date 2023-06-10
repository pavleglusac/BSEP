import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AlarmRule } from '../model/alarmRule';

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
}
