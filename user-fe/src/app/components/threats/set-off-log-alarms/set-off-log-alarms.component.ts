import { CommonModule } from "@angular/common";
import { Component, Input } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { Store } from "@ngrx/store";
import { LogAlarm } from "src/app/model/alarms";
import { AlarmAction, AlarmActionType } from "src/app/shared/store/threats-slice/threats.actions";
import { StoreType } from "src/app/shared/store/types";

@Component({
    selector: 'app-set-off-log-alarms',
    templateUrl: './set-off-log-alarms.component.html',
    standalone: true,
    styles: [],
    imports: [CommonModule, FontAwesomeModule]
  })
  export class SetOffLogAlarmsComponent {
    logAlarms: LogAlarm[] = []

    constructor(private store: Store<StoreType>) {
      this.store.subscribe((state) => {
        this.logAlarms = state.threats.logAlarms;
      });
    }

    markAsRead() {
      this.store.dispatch(new AlarmAction(AlarmActionType.MARK_AS_READ_LOG_ALARMS));
    }
  }