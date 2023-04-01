import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { CsrComponent } from "../csr/csr.component";
import { Csr } from 'src/app/model/certificate';

@Component({
    selector: 'app-csr-modal',
    standalone: true,
    templateUrl: './csr-modal.component.html',
    styles: [],
    imports: [CommonModule, DialogModule, CsrComponent]
})
export class CsrModalComponent {
  change() {
    this.visibleChange.emit(this.visible);
  }

  @Input() visible: boolean = false;

  @Output() visibleChange = new EventEmitter<boolean>();

  @Input() csr: Csr | undefined = undefined;

}
