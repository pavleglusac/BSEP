import { Component, EventEmitter, Input, Output } from '@angular/core';
import { faTemperature0, faStream, faLock, faLightbulb, faDoorClosed, faFile, faClock, faFilter, IconDefinition } from '@fortawesome/free-solid-svg-icons';
import { DialogModule } from 'primeng/dialog';
import { CommonModule } from '@angular/common';
import { Device } from 'src/app/model/myhouse';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'app-devices-modal',
  templateUrl: './devices-modal.component.html',
  styleUrls: [],
  standalone: true,
  imports: [DialogModule, CommonModule, FontAwesomeModule]
})
export class DevicesModalComponent {
  constructor() { }

  change() {
    this.visibleChange.emit(this.visible);
  }

  @Input() visible: boolean = false;

  @Output() visibleChange = new EventEmitter<boolean>();

  @Input() devices: Device[] | undefined = undefined;

  faFilePath: IconDefinition = faFile;
  faDuration: IconDefinition = faClock;
  faFilterRegex: IconDefinition = faFilter;

  hideDialog() {
    this.visible = false;
    this.change();
  }

  icons = {
    "THERMOMETER": faTemperature0,
    "MOTION DETECTOR": faStream,
    "LOCK": faLock,
    "LAMP": faLightbulb,
    "GATE": faDoorClosed
  }
}
