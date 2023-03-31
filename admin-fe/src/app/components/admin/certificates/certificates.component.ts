import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Certificate } from 'src/app/model/certificate';
import { CertificateComponent } from '../certificate/certificate.component';

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styles: [],
  imports: [CommonModule, CertificateComponent],
  standalone: true,
})
export class CertificatesComponent {
  certificates: Certificate[] = [];

  constructor() {
    for (let i = 0; i < 4; i++) {
      this.certificates.push({
        algorithm: 'SHA256withRSA',
        csrId: 'pera@gmail.com',
        hierarchyLevel: 3,
        validityStart: '2021-01-01',
        extensions: [],
        validityEnd: '2022-01-01',
        csr: undefined
      });
    }
  }
}
