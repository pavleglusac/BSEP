import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import { StoreType } from '../shared/store/types';
import { Csr } from '../model/certificate';

@Injectable({
  providedIn: 'root',
})
export class CsrService {
  constructor(private http: HttpClient) {}

  loadCsrs(successCb: (csrs: Csr[]) => void, errorCb: (error: any) => void) {
    this.http.get('api/pki/csr').subscribe({
      next(value: any) {
        console.log(value);
        successCb(value);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }
}
