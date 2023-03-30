import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Csr } from '../model/csr';

@Injectable({
  providedIn: 'root',
})
export class CsrService {
  constructor(private http: HttpClient) {}

  createCsr = (
    csr: Csr,
    successCb: () => void,
    errorCb: (error: any) => void
  ) => {
    this.http.post('api/pki/csr', csr).subscribe({
      next(value: any) {
        successCb();
      },
      error(err) {
        errorCb(err.error);
      },
    });
  };
}
