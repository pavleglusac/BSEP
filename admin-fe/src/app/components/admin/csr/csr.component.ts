import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Csr } from 'src/app/model/certificate';
import {
  CsrAction,
  CsrActionType,
} from 'src/app/shared/store/csr-slice/csr.actions';
import { StoreType } from 'src/app/shared/store/types';
import { faFileExport, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { CsrService } from 'src/app/services/csr.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-csr',
  templateUrl: './csr.component.html',
  styles: [],
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
})
export class CsrComponent {
  @Input() csr: Csr | undefined = undefined;
  @Output() reloadEmitter = new EventEmitter<string>();
  isTextVisible = false;
  denyIcon = faTrash;
  createIcon = faFileExport;

  constructor(
    private router: Router,
    private store: Store<StoreType>,
    private csrService: CsrService,
    private toastr: ToastrService
  ) {}

  onCreateCertificate = () => {
    this.store.dispatch(new CsrAction(CsrActionType.ADD, this.csr!));
    this.router.navigate(['admin/create-certificate']);
  };

  onDenyCertificate = () => {
    this.csrService.denyCsr(
      this.csr!.id,
      () => {
        this.reloadEmitter.emit('reload');
        this.router.navigate(['admin/requests']);
      },
      (err) => this.toastr.error(err.message)
    );
  };
}
