import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Certificate } from 'src/app/model/certificate';
import { CertificateComponent } from '../certificate/certificate.component';
import { CertificateService } from 'src/app/services/certificate.service';
import { CertificateCardComponent } from './certificate-card/certificate-card.component';

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styles: [],
  imports: [CommonModule, CertificateComponent, CertificateCardComponent],
  standalone: true,
})
export class CertificatesComponent implements OnInit {
  certificates: Certificate[] = [];
  showRevoked: boolean = false;

  constructor(private certificateService: CertificateService) {}

  ngOnInit() {
    this.certificateService.loadAll().subscribe((certificates: any) => {
      console.log(certificates);
      certificates.forEach((certificate: any) => {
        certificate.extensions.forEach((extension: any) => {
          extension.options.forEach((option: any) => {
            if (option.type === 'checkbox') {
              option.value = option.value === 'true';
            } else if (option.type === 'number') {
              option.value = parseInt(option.value);
            }
          });
        });
      });
      this.certificates = certificates;
    });
  }

  handleRevoke(event: any) {}

  getCertificates(): Certificate[] {
    const certificates: Certificate[] = this.showRevoked
      ? this.certificates
      : this.certificates.filter((x) => !x.isRevoked);
    return certificates;
  }
}
