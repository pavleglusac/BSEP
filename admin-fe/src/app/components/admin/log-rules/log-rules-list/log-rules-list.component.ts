import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LogRule } from 'src/app/model/logRule';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { LogService } from 'src/app/services/log.service';

@Component({
  selector: 'app-log-rules-list',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './log-rules-list.component.html',
  styles: [
  ]
})
export class LogRulesListComponent {
  @Input() logRules!: LogRule[];

  faTrash: IconDefinition = faTrash;

  constructor(private logService: LogService, private toastr: ToastrService) { }
 
  handleDeleteRule(ruleName: string): void {
    this.logService.deleteLogRule(
      ruleName,
      () => {
        this.toastr.success('Success!');
        this.logRules = this.logRules.filter(x => x.name !== ruleName);
      },
      (err) => this.toastr.error(err.message)
    )
  }

}
