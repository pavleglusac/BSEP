import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/user.service';
import { UserSearchComponent } from './user-search/user-search.component';
import { UserEntryComponent } from './user-entry/user-entry.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faChevronLeft, faChevronRight } from '@fortawesome/free-solid-svg-icons';

const amountPerPage: number = 12;

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, UserSearchComponent, UserEntryComponent, FontAwesomeModule],
  templateUrl: './users.component.html',
  styles: [
  ]
})
export class UsersComponent {
  faChevronLeft: IconDefinition = faChevronLeft;
  faChevronRight: IconDefinition = faChevronRight;

  users: User[] = [];
  page: number = 1;
  pageInfo: any = undefined;
  lastSearch: any = null;

  constructor(private userService: UserService) {
    this.search('');
  }

  search(query: string, roles: string[] | null = null, onlyLocked: boolean = false): void {
    this.lastSearch = { query, roles, onlyLocked };
    this.userService.search(query, this.page - 1, amountPerPage, roles, onlyLocked).subscribe((retval: any) => {
      if (retval) {
        this.users = retval.content;
        this.pageInfo = retval;
      }
    });
  }

  prevPage(): void {
    if (this.page > 1) {
      this.page -= 1;
      this.search(this.lastSearch.query, this.lastSearch.roles, this.lastSearch.onlyLocked);
    }
  }

  nextPage(): void {
    if (this.page < this.pageInfo.totalPages) {
      this.page += 1;
      this.search(this.lastSearch.query, this.lastSearch.roles, this.lastSearch.onlyLocked);
    }
  }
}