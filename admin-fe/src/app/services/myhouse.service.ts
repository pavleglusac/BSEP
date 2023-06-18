import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Device, RealEstate } from '../model/myHouse';
import { Tenant } from '../model/user';

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

  addNewTenant(
    email: string,
    realEstateId: string,
    successCb: (tenant: Tenant) => void,
    errorCb: (error: any) => void
  ) {
    this.http.post(`api/myhouse/tenant`, {email, realEstateId}).subscribe({
      next(req: any) {
        successCb(req);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }

  addNewDevice(
    id: string,
    device: Device,
    successCb: (value: Device) => void,
    errorCb: (err:any) => void
  ) {
    this.http.post(`api/myhouse/device`, {houseId: id, ...device}).subscribe({
      next(req: any) {
        successCb(req);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }
}
