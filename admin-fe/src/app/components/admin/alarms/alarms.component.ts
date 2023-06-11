import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import { AlarmRulesComponent } from './alarm-rules/alarm-rules.component';
import { AlarmService } from 'src/app/services/alarm.service';
import { ToastrService } from 'ngx-toastr';
import { AlarmRule } from 'src/app/model/alarmRule';

@Component({
  selector: 'app-alarms',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, AlarmRulesComponent],
  templateUrl: './alarms.component.html',
  styles: [
  ]
})
export class AlarmsComponent implements OnInit {
  faPlus: IconDefinition = faPlus;

  alarmRules: AlarmRule[] = [];

  constructor(
    private router: Router,
    private alarmService: AlarmService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.alarmService.getAlarmRules(
      (alarmRules: AlarmRule[]) => {
        this.alarmRules = alarmRules;
      },
      (err) => this.toastr.error(err.message)
    );
  }

  toAlarmCreation = () => {
    this.router.navigate(['admin/alarms/new']);
  }
}
