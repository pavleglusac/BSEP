import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Certificate } from '../model/certificate';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {


  constructor(private httpClient: HttpClient) {

  }

  approveCertificate(certificate: Certificate): Observable<any> {
    return this.httpClient.post('api/pki/certificate', certificate, { responseType: 'text' });
  }

  loadForUser(email: string) {
    return this.httpClient.get(`api/pki/csr/${email}`);
  }

  loadAll() {
    return this.httpClient.get('api/pki/certificate');
  }

  revokeCertificate(email: string, serialNumber: number): Observable<any> {
    return this.httpClient.post('api/pki/certificate-revocation', { email, serialNumber }, { responseType: 'text' });
  }
  
  validateCertificate(email: string): Observable<any> {
    return this.httpClient.get(`api/pki/validate/${email}`);
  }

}
