import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ThreatService {
  constructor(private http: HttpClient) {}

  getAlarms(
    page: number,
    successCb: (alarmsPage: any) => void,
    errorCb: (error: any) => void
  ) {
    this.http.get(`api/myhouse/alarm/mine?page=${page}&amount=13`).subscribe({
      next(res: any) {
        successCb(res);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  getMessageAlarms(
    page: number,
    successCb: (alarmsPage: any) => void,
    errorCb: (error: any) => void
  ) {
    this.http.get(`api/myhouse/alarm/mine/messages?page=${page}&amount=13`).subscribe({
      next(res: any) {
        successCb(res);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  
 
}
