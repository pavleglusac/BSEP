import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Csr } from 'src/app/model/certificate';
import { CsrService } from 'src/app/services/csr.service';
import { CsrComponent } from '../csr/csr.component';

@Component({
  selector: 'app-csrs',
  templateUrl: './csrs.component.html',
  styles: [],
  standalone: true,
  imports: [CommonModule, CsrComponent],
})
export class CsrsComponent implements OnInit {
  csrs: Csr[] = [];

  constructor(private csrService: CsrService, private toastr: ToastrService) {}
  ngOnInit(): void {
    this.csrService.loadCsrs(
      (csrs) => {
        this.csrs = csrs;
      },
      (error: any) => {}
    );
  }
}
