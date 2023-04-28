import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faLock,
  faTrash,
  faUserEdit,
  faHouseMedical,
  faArrowDownUpAcrossLine,
} from '@fortawesome/free-solid-svg-icons';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-user-entry',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './user-entry.component.html',
  styles: [],
})
export class UserEntryComponent {
  @Input() user!: User;
  faLock: IconDefinition = faLock;
  faTrash: IconDefinition = faTrash;
  faUserEdit: IconDefinition = faUserEdit;
  faHouse: IconDefinition = faHouseMedical;

  constructor(private router: Router) {}

  handleDelete(): void {}

  handleChangeRole(): void {}

  handleRealEstate(): void {
    this.router.navigate([`admin/settings/${this.user!.email}`]);
  }
}
