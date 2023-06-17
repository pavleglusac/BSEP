import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Alarm } from 'src/app/model/alarm';
import { AlarmService } from 'src/app/services/alarm.service';
import { ToastrService } from 'ngx-toastr';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { WebSocketService } from 'src/app/services/websocket.service';
import { StoreType } from 'src/app/shared/store/types';
import { Store } from '@ngrx/store';
import { AlarmAction, AlarmActionType } from 'src/app/shared/store/alarms-slice/alarms.actions';

@Component({
  selector: 'app-alarms',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './alarms.component.html',
  styles: [
  ]
})
export class AlarmsComponent implements OnInit {

  faChevronLeft: IconDefinition = faChevronLeft;
  faChevronRight: IconDefinition = faChevronRight;

  alarms: Alarm[] = [];
  pageInfo: any = null;
  page: number = 1;
  expandedAlarms: Alarm[] = [];

  constructor(
    private alarmService: AlarmService,
    private toastr: ToastrService,
    private store: Store<StoreType>
  ) {}

  ngOnInit(): void {
    this.store.dispatch(new AlarmAction(AlarmActionType.RESET_UNREAD_ALARMS));
    this.fetchAlarms();
  }

  fetchAlarms() {
    this.alarmService.getAlarms(
      this.page - 1,
      (alarmsPage: any) => {
        this.alarms = alarmsPage.content;
        this.pageInfo = alarmsPage;
      },
      (err) => this.toastr.error(err.message)
    );
  }

  prevPage(): void {
    if (this.page > 1) {
      this.page -= 1;
      this.expandedAlarms = [];
      this.fetchAlarms();
    }
  }

  nextPage(): void {
    if (this.page < this.pageInfo.totalPages) {
      this.page += 1;
      this.expandedAlarms = [];
      this.fetchAlarms();
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
