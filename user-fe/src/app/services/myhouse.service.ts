import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RealEstate } from '../model/myhouse';

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

}
