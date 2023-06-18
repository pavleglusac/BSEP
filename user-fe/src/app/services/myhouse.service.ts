import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RealEstate } from '../model/myhouse';
import { Filter } from '../components/user/messages/filter/filter.component';

@Injectable({
  providedIn: 'root',
})
export class MyHouseService {
  constructor(private http: HttpClient) {}

  loadRealEstates(
    email: string,
    successCb: (realEstates: RealEstate[]) => void,
    errorCb: (error: any) => void
  ) {
    this.http.get(`api/myhouse/realestate/${email}`).subscribe({
      next(req: any) {
        successCb(req);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  searchMessages(
    id: string,
    page: number,
    amount: number,
    filters: string,
    successCb: (realEstates: any) => void,
    errorCb: (error: any) => void
  ) {
    this.http.get(`api/myhouse/device/${id}/messages?page=${page}&amount=${amount}&${filters}`).subscribe({
      next(req: any) {
        successCb(req);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  getReports(
    id: string,
    from: Date | null,
    to: Date | null,
    type: string,
    successCb: (realEstates: any) => void,
    errorCb: (error: any) => void
  ) {
    let params = ''
    if (from && to) {
      params =  `?from=${from}&to=${to}`  
    }
    this.http.get(`api/myhouse/${type}/${id}/report${params}`).subscribe({
      next(req: any) {
        successCb(req);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }
}
