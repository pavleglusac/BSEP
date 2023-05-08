import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { User } from 'src/app/model/user';
import { MyHouseService } from 'src/app/services/myhouse.service';
import { StoreType } from 'src/app/shared/store/types';

@Component({
  selector: 'app-realestates',
  templateUrl: './realestates.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule]
})
export class RealEstatesComponent implements OnInit {
  user: User | null = null;

  constructor(private houseService: MyHouseService, private store: Store<StoreType>) {

   }

  ngOnInit(): void {
  }

}
