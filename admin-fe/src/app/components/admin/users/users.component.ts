import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/user.service';
import { UserSearchComponent } from './user-search/user-search.component';

const amountPerPage: number = 12;

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, UserSearchComponent],
  templateUrl: './users.component.html',
  styles: [
  ]
})
export class UsersComponent {
  users: User[] = [];
  page: number = 0;

  constructor(private userService: UserService) {
    this.search('');
  }

  search(query: string, roles: string[] | null = null, onlyLocked: boolean = false): void {
    const that = this;
    const d = this.userService.searchUsers(query, this.page, amountPerPage, roles, onlyLocked);
    d.subscribe((retval: any) => {
      if (retval) that.users = retval.content;
      console.log(that.users);
    });
  }
}