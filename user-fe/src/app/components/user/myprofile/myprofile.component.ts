import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { User } from 'src/app/model/user';
import { StoreType } from 'src/app/shared/store/types';

@Component({
  selector: 'app-myprofile',
  templateUrl: './myprofile.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule]
})
export class MyprofileComponent {
  user: User | null = null;

  constructor( private store: Store<StoreType>) {
    store.subscribe(state => {
      this.user = state.loggedUser.user;
    })
   }
}
