import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { Store, StoreModule } from '@ngrx/store';
import { CertificateStateType, selectCertificateFeature } from 'src/app/shared/store/certificate-slice/certificate.reducer';
import { EditExtensionAction, EditExtensionActionType } from 'src/app/shared/store/certificate-slice/certificate.actions';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import * as _ from 'lodash';

@Component({
  selector: 'app-edit-extension',
  templateUrl: './edit-extension.component.html',
  standalone: true,
  styles: [],
  imports: [DialogModule, ButtonModule, CommonModule, FormsModule],
})
export class EditExtensionComponent {

  extension: any = undefined;

  visible: boolean = false;

  constructor(private store: Store<CertificateStateType>) {
    this.store.select(selectCertificateFeature).subscribe((state: CertificateStateType) => {
      if(!state) return;
      
      this.extension = _.cloneDeep(state.editedExtension);
      this.visible = state.showEditingModal;
    });
  }

  hideDialog() {
    this.store.dispatch(new EditExtensionAction(EditExtensionActionType.RESET, undefined));
    this.store.dispatch(new EditExtensionAction(EditExtensionActionType.TOGGLE_MODAL, false));
  }

  updateValue() {
    this.store.dispatch(new EditExtensionAction(EditExtensionActionType.SET, this.extension));
  }

}
