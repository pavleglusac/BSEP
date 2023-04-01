import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Csr } from 'src/app/model/certificate';
import {
  CsrAction,
  CsrActionType,
} from 'src/app/shared/store/csr-slice/csr.actions';
import { StoreType } from 'src/app/shared/store/types';

@Component({
  selector: 'app-csr',
  templateUrl: './csr.component.html',
  styles: [],
  standalone: true,
  imports: [CommonModule],
})
export class CsrComponent {
  @Input() csr: Csr | undefined = undefined;
  isTextVisible = false;

  constructor(private router: Router, private store: Store<StoreType>) {}

  onCreateCertificate = () => {
    this.store.dispatch(new CsrAction(CsrActionType.ADD, this.csr!));
    this.router.navigate(['admin/create-certificate']);
  };
}
