import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { IconDefinition, faChevronLeft, faChevronRight } from "@fortawesome/free-solid-svg-icons";
import { Store } from "@ngrx/store";
import { ToastrService } from "ngx-toastr";
import { Alarm } from "src/app/model/alarms";
import { ThreatService } from "src/app/services/threat.service";
import { AlarmAction, AlarmActionType } from "src/app/shared/store/threats-slice/threats.actions";
import { StoreType } from "src/app/shared/store/types";

@Component({
    selector: 'app-set-off-alarms',
    templateUrl: './set-off-alarms.component.html',
    standalone: true,
    styles: [],
    imports: [CommonModule, FontAwesomeModule]
  })
  export class SetOffAlarmsComponent implements OnInit{

    faChevronLeft: IconDefinition = faChevronLeft;
    faChevronRight: IconDefinition = faChevronRight;
  
    alarms: Alarm[] = []
    pageInfo: any = null;
    page: number = 1;
    expandedAlarms: Alarm[] = [];

    constructor(private store: Store<StoreType>, private threatService: ThreatService, private toastr: ToastrService) {
      this.store.subscribe((state) => {
        this.alarms = state.threats.alarms;
        this.pageInfo = state.threats.pageInfoAlarms;
        this.page = state.threats.pageInfoAlarms.number + 1;
      });
    }
  ngOnInit(): void {
    this.store.dispatch(new AlarmAction(AlarmActionType.SET_READ_MESSAGES_ALARMS));
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
      this.store.dispatch(new AlarmAction(AlarmActionType.SET_READ_MESSAGES_ALARMS));
    }

    fetchAlarms() {
      this.threatService.getAlarms(
        this.page - 1,
        (alarmsPage: any) => {
          this.store.dispatch(new AlarmAction(AlarmActionType.ADD_ALARMS, alarmsPage))
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