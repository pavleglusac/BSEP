import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { IconDefinition, faChevronDown, faChevronLeft, faChevronRight, faChevronUp } from "@fortawesome/free-solid-svg-icons";
import { Store } from "@ngrx/store";
import { ToastrService } from "ngx-toastr";
import { MessageAlarm } from "src/app/model/alarms";
import { ThreatService } from "src/app/services/threat.service";
import { AlarmAction, AlarmActionType } from "src/app/shared/store/threats-slice/threats.actions";
import { StoreType } from "src/app/shared/store/types";

@Component({
    selector: 'app-message-alarms',
    templateUrl: './message-alarms.component.html',
    standalone: true,
    styles: [],
    imports: [CommonModule, FontAwesomeModule]
  })
  export class MessageAlarmsComponent implements OnInit{
    faChevronLeft: IconDefinition = faChevronLeft;
    faChevronRight: IconDefinition = faChevronRight;
    faChevronDown: IconDefinition = faChevronDown;
    faChevronUp: IconDefinition = faChevronUp;

    messages: MessageAlarm[] = []
    pageInfo: any = null;
    page: number = 1;
    expandedAlarms: MessageAlarm[] = [];

    constructor(private store: Store<StoreType>, private threatService: ThreatService, private toastr: ToastrService) {
      this.store.subscribe((state) => {
        this.messages = state.threats.messagesAlarm;
        this.pageInfo = state.threats.pageInfoMessagesAlarms;
        this.page = state.threats.pageInfoMessagesAlarms.number + 1;
      });
    }

    ngOnInit(): void {
      this.store.dispatch(new AlarmAction(AlarmActionType.SET_READ_MESSAGES_MESSAGES_ALARMS));
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
      this.store.dispatch(new AlarmAction(AlarmActionType.SET_READ_MESSAGES_MESSAGES_ALARMS));
    }

    fetchMessageAlarms() {
      this.threatService.getMessageAlarms(
        this.page - 1,
        (alarmsPage: any) => {
          this.store.dispatch(new AlarmAction(AlarmActionType.ADD_MESSAGES_ALARMS, alarmsPage))
        },
        (err) => this.toastr.error(err.message)
      );
    }

    prevPage(): void {
      if (this.page > 1) {
        this.page -= 1;
        this.expandedAlarms = [];
        this.fetchMessageAlarms();
      }
    }
  
    nextPage(): void {
      if (this.page < this.pageInfo.totalPages) {
        this.page += 1;
        this.expandedAlarms = [];
        this.fetchMessageAlarms();
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