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

  constructor(private certificateService: CertificateService) {
   
  }

  ngOnInit() {
    this.certificateService.loadAll().subscribe((certificates: any) => {
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

}
