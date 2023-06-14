import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LogsComponent } from '../logs.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { FormsModule } from '@angular/forms';



@Component({
  selector: 'app-log-search',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, FormsModule],
  templateUrl: './log-search.component.html',
  styles: [
  ]
})
export class LogSearchComponent {
  @Input() parent!: LogsComponent;
  faSearch: IconDefinition = faSearch;

  logTypes: string[] = [
    "ALL",
    "ERROR",
    "WARNING",
    "SUCCESS",
    "INFO"
  ];

  actionQuery: string = '';
  detailsQuery: string = '';
  ipAddressQuery: string = '';
  logType: string = this.logTypes[0];
  usernames: string[] = [];
  username: string = '';

  showActionQueryError: boolean = false;
  showDetailsQueryError: boolean = false;
  showIpAddressQueryError: boolean = false;

  enableRegex: boolean = false;

  handleSearch(): void {
    this.parent.search(this.actionQuery, this.detailsQuery, this.ipAddressQuery, this.logType !== 'ALL' ? this.logType : null, this.usernames, this.enableRegex);
  }

  handleUsernameInput(): void {
    const value = this.username.slice(0, this.username.length - 1).replaceAll(',', '') + this.username.slice(this.username.length - 1, this.username.length);
    if (value && value.length > 1 && value.slice(value.length - 1, value.length) === ',') {
      this.username = '';
      const toAdd = value.slice(0, value.length - 1);
      if (!this.usernames.includes(toAdd)) {
        this.usernames.push(toAdd);
      }
    }
  }

  removeUsername(uname: string): void {
    this.usernames = this.usernames.filter(x => x !== uname)
  }

  logTypeChanged(event: any) {
    this.logType = event.target.value.toUpperCase().replaceAll(' ', '_');
  }
}
