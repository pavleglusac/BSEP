import { Component, Input, OnInit } from '@angular/core';
import { extensions, templates } from './extensions';
import { CommonModule } from '@angular/common';
import * as _ from 'lodash';
import { EditExtensionComponent } from './edit-extension/edit-extension.component';
import {
  CertificateStateType,
  selectCertificateFeature,
} from 'src/app/shared/store/certificate-slice/certificate.reducer';
import { Store } from '@ngrx/store';
import {
  EditExtensionAction,
  EditExtensionActionType,
} from 'src/app/shared/store/certificate-slice/certificate.actions';
import { FormsModule } from '@angular/forms';
import { CertificateService } from 'src/app/services/certificate.service';
import { Certificate } from 'src/app/model/certificate';
import { Toast, ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  standalone: true,
  styles: [],
  imports: [CommonModule, EditExtensionComponent, FormsModule],
})
export class CertificateComponent implements OnInit {

  @Input() readonly = false;
  @Input() certificate: Certificate | undefined = undefined;
  
  addedExtensions: any = [];
  selectedTemplate = 'CA';
  modalVisible = false;
  editedExtension = undefined;
  validityAmount = 1;
  validityUnit = 'years';
  email = '';
  canProcess = false;

  today = new Date();
  until = new Date(
    this.today.getFullYear() + 1,
    this.today.getMonth(),
    this.today.getDate()
  );
  hierarchyLevel: number = 3;

  constructor(
    private store: Store<CertificateStateType>,
    private certificateService: CertificateService,
    private toastr: ToastrService
  ) {
    this.store
      .select(selectCertificateFeature)
      .subscribe((state: CertificateStateType) => {
        if (!state) return;
        if (!state.editedExtension) return;
        for (let i = 0; i < this.addedExtensions.length; i++) {
          if (this.addedExtensions[i].name === state.editedExtension.name) {
            this.addedExtensions[i] = _.cloneDeep(state.editedExtension);
            break;
          }
        }
      });
    

  }

  ngOnInit() {
    if (this.certificate) {
      this.addedExtensions = this.certificate.extensions;
      this.today = new Date(this.certificate.validityStart);
      this.until = new Date(this.certificate.validityEnd);
      this.hierarchyLevel = this.certificate.hierarchyLevel;
      this.email = this.certificate.csrId;
    }
  }

  approveCertificate() {
    if (!this.canProcess) {
      this.toastr.error('Certificate cannot be created! Load CSR first!');
    }
    let extensions = [];
    // add extensions from addedExtensions but stringify value field
    for (let i = 0; i < this.addedExtensions.length; i++) {
      let ext = _.cloneDeep(this.addedExtensions[i]);
      // iterate over options and stringify value field
      for (let j = 0; j < ext.options.length; j++) {
        ext.options[j].value = ext.options[j].value.toString();
      }
      extensions.push(ext);
    }
    let certificate: Certificate = {
      algorithm: 'SHA256withRSA',
      csrId: this.email,
      validityStart: this.today.toISOString(),
      validityEnd: this.until.toISOString(),
      extensions: extensions,
      hierarchyLevel: this.hierarchyLevel,
      csr: undefined,
    };
    this.certificateService.approveCertificate(certificate).subscribe({
      next: (data) => {
        this.toastr.success('Certificate successfully created!');
      },
      error: (error) => {
        this.toastr.error('Error creating certificate!');
        console.log(error);
      },
    });
  }

  applyValidity() {
    if (this.validityUnit === 'days') {
      this.until = new Date(
        this.today.getFullYear(),
        this.today.getMonth(),
        this.today.getDate() + this.validityAmount
      );
      return;
    }
    if (this.validityUnit === 'months') {
      this.until = new Date(
        this.today.getFullYear(),
        this.today.getMonth() + this.validityAmount,
        this.today.getDate()
      );
      return;
    }
    this.until = new Date(
      this.today.getFullYear() + this.validityAmount,
      this.today.getMonth(),
      this.today.getDate()
    );
  }

  loadTemplate() {
    this.addedExtensions = templates[this.selectedTemplate].map(
      (extension: string) => _.cloneDeep(extensions[extension])
    );
  }

  loadForUser() {
    this.certificateService.loadForUser(this.email).subscribe({
      next: (data) => {
        console.log(data);
        this.toastr.success('Certificate requests loaded!');
        this.canProcess = true;
      },
      error: (error) => {
        alert('Error loading certificate requests for user!');
        console.log(error);
        this.canProcess = false;
      }
    });
  }

  onTemplateSelected(selectedTemplate: string) {
    this.selectedTemplate = selectedTemplate;
  }

  editExtension(extension: string) {
    this.modalVisible = true;
    this.editedExtension = this.addedExtensions.filter(
      (ext: any) => ext.name === extension
    )[0];
    this.store.dispatch(
      new EditExtensionAction(EditExtensionActionType.SET, this.editedExtension)
    );
    this.store.dispatch(
      new EditExtensionAction(
        EditExtensionActionType.TOGGLE_MODAL,
        this.modalVisible
      )
    );
  }

  removeExtension(extension: string) {
    for (let i = 0; i < this.addedExtensions.length; i++) {
      if (this.addedExtensions[i].name === extension) {
        this.addedExtensions.splice(i, 1);
        break;
      }
    }
  }
}
