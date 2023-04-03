import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Certificate } from '../model/certificate';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CertificateService {
  constructor(private httpClient: HttpClient) {}

  approveCertificate(certificate: Certificate): Observable<any> {
    return this.httpClient.post('api/pki/certificate', certificate, {
      responseType: 'text',
    });
  }

  loadForUser(email: string) {
    return this.httpClient.get(`api/pki/csr/${email}`);
  }

  loadAll() {
    return this.httpClient.get('api/pki/certificate');
  }

  revokeCertificate(email: string): Observable<any> {
    return this.httpClient.post(
      'api/pki/certificate-revocation',
      { email },
      { responseType: 'text' }
    );
  }

  validateCertificate(email: string): Observable<any> {
    return this.httpClient.get(`api/pki/validate/${email}`);
  }

  distributeCertificate(
    email: string,
    successCb: (value: any) => void,
    errorCb: (error: any) => void
  ) {
    this.httpClient.get(`api/pki/certificate/distribute/${email}`).subscribe({
      next(value: any) {
        successCb(value.message);
      },
      error(err) {
        errorCb(err.error);
      },
    });
  }
}
