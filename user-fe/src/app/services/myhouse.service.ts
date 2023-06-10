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

}
