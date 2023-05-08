import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Csr } from 'src/app/model/csr';
import { CsrService } from 'src/app/services/csr.service';

@Component({
  selector: 'app-csr',
  templateUrl: './csr.component.html',
  styleUrls: ['./csr.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule],
})
export class CsrComponent {
  csr: Csr = new Csr();

  constructor(private csrService: CsrService, private toastr: ToastrService) {}

  onCreate = () => {
    this.csrService.createCsr(
      this.csr,
      () => this.toastr.success('Certificate signing request created.'),
      (error: any) =>
        this.toastr.error(
          error.message,
          'Creating certificate signing request failed.'
        )
    );
  };
}
