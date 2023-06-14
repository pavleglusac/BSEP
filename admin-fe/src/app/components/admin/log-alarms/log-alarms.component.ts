import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Alarm, LogAlarm } from 'src/app/model/alarm';
import { AlarmService } from 'src/app/services/alarm.service';
import { ToastrService } from 'ngx-toastr';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { Log } from 'src/app/model/log';

@Component({
  selector: 'app-log-alarms',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './log-alarms.component.html',
  styles: [
  ]
})
export class LogAlarmsComponent implements OnInit {
  faChevronLeft: IconDefinition = faChevronLeft;
  faChevronRight: IconDefinition = faChevronRight;

  alarms: LogAlarm[] = [];
  pageInfo: any = null;
  page: number = 1;
  expandedAlarms: Log[] = [];

  constructor(
    private alarmService: AlarmService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.fetchLogAlarms();
  }

  fetchLogAlarms() {
    this.alarmService.getLogAlarms(
      this.page - 1,
      (logAlarmsPage: any) => {
        this.alarms = logAlarmsPage.content;
        this.pageInfo = logAlarmsPage;
      },
      (err) => this.toastr.error(err.message)
    );
  }


  prevPage(): void {
    if (this.page > 1) {
      this.page -= 1;
      this.expandedAlarms = [];
      this.fetchLogAlarms();
    }
  }

  nextPage(): void {
    if (this.page < this.pageInfo.totalPages) {
      this.page += 1;
      this.expandedAlarms = [];
      this.fetchLogAlarms();
    }
  }

  handleExpand(alarm: any): void {
    if (!this.expandedAlarms.includes(alarm))
      this.expandedAlarms.push(alarm);
    else
      this.expandedAlarms = this.expandedAlarms.filter(x => x !== alarm);
  }

  isExpanded(alarm: any) {
    return this.expandedAlarms.includes(alarm);
  }

}
