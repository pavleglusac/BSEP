import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlarmRule } from 'src/app/model/alarmRule';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { AlarmService } from 'src/app/services/alarm.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-alarm-rules-list',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './alarm-rules-list.component.html',
  styles: [
  ]
})
export class AlarmRulesListComponent {
  @Input() alarmRules!: AlarmRule[];

  faTrash: IconDefinition = faTrash;

  constructor(private alarmService: AlarmService, private toastr: ToastrService) {}

  handleDeleteRule(ruleName: string): void {
    this.alarmService.deleteAlarmRule(
      ruleName,
      () => {
        this.toastr.success('Success!');
        this.alarmRules = this.alarmRules.filter(x => x.name !== ruleName);
      },
      (err) => this.toastr.error(err.message)
    )
  }
}
