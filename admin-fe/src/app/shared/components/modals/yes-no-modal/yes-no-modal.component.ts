import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  standalone: true,
  imports: [CommonModule],
  selector: 'app-yes-no-modal',
  templateUrl: './yes-no-modal.component.html',
  styles: [
  ]
})
export class YesNoModalComponent {
  @Input() title!: string;
  @Input() description!: string;
  @Output() yes: EventEmitter<void> = new EventEmitter();
  @Output() no: EventEmitter<void> = new EventEmitter();

  handleYes () {
    this.yes.emit();
  }

  handleNo () {
    this.no.emit();
  }

}
