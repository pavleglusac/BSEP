import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faCertificate,
  faUser,
  faPenNib,
} from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { ToastrService } from 'ngx-toastr';
import { Certificate } from 'src/app/model/certificate';
import { CertificateService } from 'src/app/services/certificate.service';
import { YesNoModalComponent } from 'src/app/shared/components/modals/yes-no-modal/yes-no-modal.component';
import { CertificateHierarchyLevelPipe } from 'src/app/shared/pipes/certificate-hierarchy-level.pipe';
import { EditExtensionAction, EditExtensionActionType } from 'src/app/shared/store/certificate-slice/certificate.actions';
import { CertificateStateType } from 'src/app/shared/store/certificate-slice/certificate.reducer';
import { EditExtensionComponent } from '../../certificate/edit-extension/edit-extension.component';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule,
    CertificateHierarchyLevelPipe,
    YesNoModalComponent,
    EditExtensionComponent,
  ],
  selector: 'app-certificate-card',
  templateUrl: './certificate-card.component.html',
  styles: [],
})
export class CertificateCardComponent {
  @Input() certificate: Certificate | undefined = undefined;

  faCertificate: IconDefinition = faCertificate;
  faUser: IconDefinition = faUser;
  faPenNib: IconDefinition = faPenNib;
  showRevokeYesNoModal: boolean = false;

  editedExtension = undefined;

  constructor(
    private certificateService: CertificateService,
    private toastr: ToastrService,
    private store: Store<CertificateStateType>,
  ) {}

  revokeCertificate(): void {
    if (this.certificate?.csrId && this.certificate.serialNumber) {
      this.certificateService.revokeCertificate(this.certificate.csrId, this.certificate.serialNumber).subscribe({
        next: (data) => {
          this.toastr.success('Certificate successfully revoked!');
          this.certificate!.isRevoked = true;
          this.closeRevokeYesNoModal();
        },
        error: (error) => {
          this.toastr.error('Error revoking certificate!');
          console.log(error);
        },
      });
    }
  }

  validateCertificate(): void {
    if (!this.certificate || this.certificate.serialNumber === undefined) { 
      return;
    }
    this.certificateService.validateCertificate(this.certificate.serialNumber).subscribe({
      next: (data) => {
        console.log(data);
        if (data)
          this.toastr.success('Certificate is valid.');
        else
          this.toastr.error('Certificate is invalid.');
      },
      error: (error) => {
        this.toastr.error('Error while checking certificate validity!');
        console.log(error);
      },
    });
  }

  distributeCertificate(): void {
    this.certificateService.distributeCertificate(
      this.certificate!.csr!.email,
      (message) => this.toastr.success(message),
      (err) => this.toastr.error(err.message)
    );
  }

  viewCSR(): void {
    // TODO: show CSR
  }

  closeRevokeYesNoModal(): void {
    this.showRevokeYesNoModal = false;
  }

  showExtensionModal(extension: string) {
    this.editedExtension = this.certificate?.extensions.filter(
      (ext: any) => ext.name === extension
    )[0];
    this.store.dispatch(
      new EditExtensionAction(EditExtensionActionType.SET, this.editedExtension)
    );
    this.store.dispatch(
      new EditExtensionAction(
        EditExtensionActionType.TOGGLE_MODAL,
        true
      )
    );
  }
}
