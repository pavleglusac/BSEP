import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Log } from 'src/app/model/log';

@Component({
  selector: 'app-log-entry',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './log-entry.component.html',
  styles: [
  ]
})
export class LogEntryComponent {
  @Input() log!: Log;
}
