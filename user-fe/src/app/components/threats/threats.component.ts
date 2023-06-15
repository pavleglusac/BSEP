import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { Alarm, LogAlarm, MessageAlarm } from "src/app/model/alarms";
import { MessageAlarmsComponent } from "./message-alarms/message-alarms.component";
import { SetOffLogAlarmsComponent } from "./set-off-log-alarms/set-off-log-alarms.component";
import { SetOffAlarmsComponent } from "./set-off-alarms/set-off-alarms.component";
import { Store } from "@ngrx/store";
import { StoreType } from "src/app/shared/store/types";
import { AlarmAction, AlarmActionType } from "src/app/shared/store/threats-slice/threats.actions";

@Component({
    selector: 'app-threats',
    templateUrl: './threats.component.html',
    standalone: true,
    styles: [],
    imports: [CommonModule, FontAwesomeModule,MessageAlarmsComponent, SetOffLogAlarmsComponent, SetOffAlarmsComponent]
  })
  export class ThreatsComponent{
      selected = 1;
      
      changeSelect(num: number){
          this.selected = num;
        }


}