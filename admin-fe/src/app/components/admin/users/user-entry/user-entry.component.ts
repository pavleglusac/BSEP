import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faLock, faTrash, faUserEdit } from '@fortawesome/free-solid-svg-icons';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-user-entry',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
  templateUrl: './user-entry.component.html',
  styles: [
  ]
})
export class UserEntryComponent {
  @Input() user!: User;
  faLock: IconDefinition = faLock;
  faTrash: IconDefinition = faTrash;
  faUserEdit: IconDefinition = faUserEdit;

  constructor() { }

  handleDelete(): void {

  }

  handleChangeRole(): void {
    
  }
}
