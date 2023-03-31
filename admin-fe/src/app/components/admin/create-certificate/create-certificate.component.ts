import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CertificateComponent } from '../certificate/certificate.component';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, CertificateComponent],
  templateUrl: './create-certificate.component.html',
  styles: [
  ]
})
export class CreateCertificateComponent {

}
