import { Injectable } from '@angular/core';
import { LogRule } from '../model/logRule';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LogService {

  constructor(private http: HttpClient) {}

  search(
    actionQuery: string,
    detailsQuery: string,
    ipAddressQuery: string,
    logType: string | null,
    usernames: string[] | null = null,
    regexEnabled: boolean = false,
    page: number,
    amount: number
  ): Observable<any> {
    return this.http.get(`api/log?page=${page}&amount=${
      amount}${actionQuery ? '&actionQuery=' + encodeURIComponent(actionQuery) : ''}${
        detailsQuery ? '&detailsQuery=' + encodeURIComponent(detailsQuery) : ''}${
          ipAddressQuery ? '&ipAddressQuery=' + encodeURIComponent(ipAddressQuery) : ''}${
            logType ? '&logType=' + logType : ''}${
              '&regexEnabled=' + regexEnabled}${
                usernames && usernames.length > 0 ? '&usernames=' + usernames.join(',') : ''
              }`);
  }

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

  addLogRule(
    logRule: LogRule,
    successCb: () => void,
    errorCb: (error: any) => void
  ) {
    this.http.post(`api/log/rules`, logRule).subscribe({
      next(res: any) {
        successCb();
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  deleteLogRule(ruleName: string, successCb: () => void, errorCb: (error: any) => void) {
    this.http.delete(`api/log/rules/${ruleName}`).subscribe({
      next(value: any) {
        successCb();
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }
}
