import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LogRulesListComponent } from './log-rules-list/log-rules-list.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';
import { LogService } from 'src/app/services/log.service';
import { ToastrService } from 'ngx-toastr';
import { LogRule } from 'src/app/model/logRule';

@Component({
  selector: 'app-log-rules',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, LogRulesListComponent],
  templateUrl: './log-rules.component.html',
  styles: [
  ]
})
export class LogRulesComponent {
  faPlus: IconDefinition = faPlus;

  logRules: LogRule[] = [];

  constructor(
    private router: Router,
    private logService: LogService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.logService.getLogRules(
      (logRules: LogRule[]) => {
        this.logRules = logRules;
      },
      (err) => this.toastr.error(err.message)
    );
  }

  toLogRuleCreation = () => {
    this.router.navigate(['admin/log-rules/new']);
  }

}
