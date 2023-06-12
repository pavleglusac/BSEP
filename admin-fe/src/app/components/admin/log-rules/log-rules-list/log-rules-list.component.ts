import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LogRule } from 'src/app/model/logRule';

@Component({
  selector: 'app-log-rules-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './log-rules-list.component.html',
  styles: [
  ]
})
export class LogRulesListComponent {
  @Input() logRules!: LogRule[];

}
