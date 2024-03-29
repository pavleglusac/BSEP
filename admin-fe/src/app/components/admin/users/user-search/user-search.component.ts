import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import { UsersComponent } from '../users.component';

@Component({
  selector: 'app-user-search',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './user-search.component.html',
  styles: [
  ]
})
export class UserSearchComponent {
  @Input() parent!: UsersComponent;
  faSearch: IconDefinition = faSearch;

  query: string = '';
  selectedRoles: string[] = ['ROLE_ADMIN', 'ROLE_TENANT', 'ROLE_LANDLORD'];
  allRoles: string[] = ['ROLE_ADMIN', 'ROLE_TENANT', 'ROLE_LANDLORD'];
  onlyLocked: boolean = false;

  showQueryError: boolean = false;

  selectRole(event: any, role: string): void {
    if (event.target.checked) {
      this.selectedRoles.push(role);
    } else {
      this.selectedRoles = this.selectedRoles.filter((r) => r !== role);
    }
  }

  handleSearch(): void {
    if (this.isQueryValid())
      this.parent.search(this.query, this.selectedRoles, this.onlyLocked);
    else
      this.showQueryError = true;
  }

  isQueryValid(): boolean {
    return /^[a-zA-Z0-9.@\s]*$/.test(this.query);
  }
}
