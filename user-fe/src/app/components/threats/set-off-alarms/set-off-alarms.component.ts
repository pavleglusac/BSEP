import { CommonModule } from "@angular/common";
import { Component, Input } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { Store } from "@ngrx/store";
import { Alarm } from "src/app/model/alarms";
import { AlarmAction, AlarmActionType } from "src/app/shared/store/threats-slice/threats.actions";
import { StoreType } from "src/app/shared/store/types";

@Component({
    selector: 'app-set-off-alarms',
    templateUrl: './set-off-alarms.component.html',
    standalone: true,
    styles: [],
    imports: [CommonModule, FontAwesomeModule]
  })
  export class SetOffAlarmsComponent {

    alarms: Alarm[] = []
    constructor(private store: Store<StoreType>) {
      this.store.subscribe((state) => {
        this.alarms = state.threats.alarms;
      });
    }

    get groupedAlarms(): {[key: string]: Alarm[]} {
      return this.alarms.reduce((acc:  { [key: string]: Alarm[] }, alarm) => {
        const key: string = alarm.realEstate!.id!;
        if (!acc[key]) {
          acc[key] = [];
        }
        acc[key].push(alarm);
        return acc;
      }, {});
    }

    markAsRead() {
      this.store.dispatch(new AlarmAction(AlarmActionType.MARK_AS_READ_ALARMS));
    }
  }