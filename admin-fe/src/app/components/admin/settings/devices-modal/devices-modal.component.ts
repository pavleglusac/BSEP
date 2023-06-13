import { Component, EventEmitter, Input, Output } from '@angular/core';
import { faFile, faClock, faFilter, IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { DialogModule } from 'primeng/dialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Device, DeviceType } from 'src/app/model/myHouse';

@Component({
  selector: 'app-devices-modal',
  templateUrl: './devices-modal.component.html',
  styleUrls: ['./devices-modal.component.css'],
  standalone: true,
  imports: [DialogModule, CommonModule, FontAwesomeModule, FormsModule]
})
export class DevicesModalComponent {
  constructor() { }

  change() {
    this.visibleChange.emit(this.visible);
  }

  @Input() visible: boolean = false;

  @Output() visibleChange = new EventEmitter<boolean>();

  @Input() devices: Device[] | undefined = undefined;

  @Output() addNewDevice = new EventEmitter<Device>();

  faFilePath: IconDefinition = faFile;
  faDuration: IconDefinition = faClock;
  faFilterRegex: IconDefinition = faFilter;
  errors = {name: "", regex: "", refreshRate: ""};

  newDevice = new Device(DeviceType.THERMOMETER, '', 1, '');


  hideDialog() {
    this.visible = false;
    this.change();
  }

  handleAddDevice() {
    if (this.valid()) {
      this.addNewDevice.emit(this.newDevice);
      this.newDevice = new Device(DeviceType.THERMOMETER, '', 1, '');
    }
  }

  valid = () => {
    let valid = true;
    if (!this.newDevice.name) {
      this.errors.name = "Device's name is required"
      valid = false;
    } else {
      this.errors.name = ''
    }
    if (!this.newDevice.filterRegex) {
      this.errors.regex = "Device's regex is required"
      valid = false;
    } else {
      this.errors.regex = ''
    }
    if (!this.newDevice.refreshRate || this.newDevice.refreshRate < 1) {
      this.errors.refreshRate = "Device's refresh rate must be a positive number"
      valid = false;
    } else {
      this.errors.refreshRate = ''
    }

    return valid;
  }
  
}
