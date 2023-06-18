import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { MessageAlarmsComponent } from "./message-alarms/message-alarms.component";
import { SetOffAlarmsComponent } from "./set-off-alarms/set-off-alarms.component";
import { Store } from "@ngrx/store";
import { StoreType } from "src/app/shared/store/types";

@Component({
    selector: 'app-threats',
    templateUrl: './threats.component.html',
    standalone: true,
    styles: [],
    imports: [CommonModule, FontAwesomeModule,MessageAlarmsComponent, SetOffAlarmsComponent]
  })
  export class ThreatsComponent{
      selected = 1;

      unreadAlarms = 0;
      unreadMessageAlarms = 0;
      constructor(private store: Store<StoreType>) {
        this.store.subscribe((state) => {
          this.unreadAlarms = state.threats.unreadMessagesAlarms;
          this.unreadMessageAlarms = state.threats.unreadMessagesMessagesAlarms;
        });
      }

      changeSelect(num: number){
          this.selected = num;
      }
}