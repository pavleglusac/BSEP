import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IconDefinition, faPlus } from '@fortawesome/free-solid-svg-icons';
import { User } from 'src/app/model/user';
import { DialogModule } from 'primeng/dialog';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-tenants-modal',
  templateUrl: './tenants-modal.component.html',
  styleUrls: [],
  standalone: true,
  imports: [DialogModule, CommonModule]
})
export class TenantsModalComponent {
  constructor() { }

  change() {
    this.visibleChange.emit(this.visible);
  }

  @Input() visible: boolean = false;

  @Output() visibleChange = new EventEmitter<boolean>();

  @Input() tenants: User[] | undefined = undefined;

  @Input() realEstateId: string | undefined = undefined;

  faAdd: IconDefinition = faPlus;

  hideDialog() {
    this.visible = false;
    this.change();
  }

}
