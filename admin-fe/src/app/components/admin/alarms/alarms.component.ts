import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faPlus } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-alarms',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './alarms.component.html',
  styles: [
  ]
})
export class AlarmsComponent {
  faPlus: IconDefinition = faPlus;

  alarms = [];

  constructor(private router: Router) {}

  toAlarmCreation = () => {
    this.router.navigate(['admin/alarms/new']);
  }
}
