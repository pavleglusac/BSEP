import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RealEstate } from '../model/myHouse';

@Injectable({
  providedIn: 'root',
})
export class MyhouseService {
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

  addRealEstates(
    email: string,
    realEstate: RealEstate,
    successCb: (realEstates: RealEstate) => void,
    errorCb: (error: any) => void
  ) {
    this.http.post(`api/myhouse/realestate/${email}`, realEstate).subscribe({
      next(req: any) {
        successCb(req);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  editRealEstates(
    email: string,
    realEstate: RealEstate,
    successCb: (realEstates: RealEstate) => void,
    errorCb: (error: any) => void
  ) {
    this.http.put(`api/myhouse/realestate/${email}`, realEstate).subscribe({
      next(req: any) {
        successCb(req);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }
}
