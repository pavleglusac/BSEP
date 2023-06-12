import { Injectable } from '@angular/core';
import { LogRule } from '../model/logRule';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LogService {

  constructor(private http: HttpClient) {}

  getLogRules(
    successCb: (alarmRules: LogRule[]) => void,
    errorCb: (error: any) => void
  ) {
    this.http.get(`api/log/rules`).subscribe({
      next(res: any) {
        successCb(res);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }
}
