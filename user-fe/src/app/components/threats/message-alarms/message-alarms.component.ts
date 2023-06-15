import { CommonModule } from "@angular/common";
import { Component, Input } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { Store } from "@ngrx/store";
import { MessageAlarm } from "src/app/model/alarms";
import { AlarmAction, AlarmActionType } from "src/app/shared/store/threats-slice/threats.actions";
import { StoreType } from "src/app/shared/store/types";

@Component({
    selector: 'app-message-alarms',
    templateUrl: './message-alarms.component.html',
    standalone: true,
    styles: [],
    imports: [CommonModule, FontAwesomeModule]
  })
  export class MessageAlarmsComponent {

    messages: MessageAlarm[] = []
    constructor(private store: Store<StoreType>) {
      this.store.subscribe((state) => {
        this.messages = state.threats.messagesAlarm;
      });
    }

    get groupedMessageAlarms(): {[key: string]: MessageAlarm[]} {
      return this.messages.reduce((acc:  { [key: string]: MessageAlarm[] }, message) => {
        const key: string = message.realEstate!.id!;
        if (!acc[key]) {
          acc[key] = [];
        }
        acc[key].push(message);
        return acc;
      }, {});
    }

    markAsRead() {
      this.store.dispatch(new AlarmAction(AlarmActionType.MARK_AS_READ_MESSAGES_ALARMS));
    }
  }