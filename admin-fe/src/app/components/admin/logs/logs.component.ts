import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Log } from 'src/app/model/log';
import { LogService } from 'src/app/services/log.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { LogSearchComponent } from './log-search/log-search.component';
import { LogEntryComponent } from './log-entry/log-entry.component';

const amountPerPage: number = 20;

@Component({
  selector: 'app-logs',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, LogSearchComponent, LogEntryComponent],
  templateUrl: './logs.component.html',
  styles: [
  ]
})
export class LogsComponent {
  faChevronLeft: IconDefinition = faChevronLeft;
  faChevronRight: IconDefinition = faChevronRight;

  logs: Log[] = [];
  page: number = 1;
  lastSearch: any = null;
  totalItems: number = -1;

  constructor(private logService: LogService) {
    this.search('', '', '', null, null, false);
  }

  search(actionQuery: string, detailsQuery: string, ipAddressQuery: string, logType: string | null, usernames: string[] | null = null, regexEnabled: boolean = false): void {
    this.lastSearch = { actionQuery, detailsQuery, ipAddressQuery, logType, usernames, regexEnabled };
    this.logService.search(actionQuery, detailsQuery, ipAddressQuery, logType, usernames, regexEnabled, this.page - 1, amountPerPage)
    .subscribe((retval: any) => {
      if (retval) {
        console.log(retval);
        this.logs = retval.items;
        this.totalItems = retval.totalItems;
      }
    });
  }

  prevPage(): void {
    if (this.page > 1) {
      this.page -= 1;
      this.search(this.lastSearch.actionQuery, this.lastSearch.detailsQuery, this.lastSearch.ipAddressQuery, this.lastSearch.logType, this.lastSearch.usernames, this.lastSearch.regexEnabled);
    }
  }

  nextPage(): void {
    if (this.page < this.totalPages) {
      this.page += 1;
      this.search(this.lastSearch.actionQuery, this.lastSearch.detailsQuery, this.lastSearch.ipAddressQuery, this.lastSearch.logType, this.lastSearch.usernames, this.lastSearch.regexEnabled);
    }
  }

  get totalPages(): number {
    return Math.ceil(this.totalItems / amountPerPage);
  }

}
