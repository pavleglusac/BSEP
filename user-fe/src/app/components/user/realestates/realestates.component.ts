import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { ToastrService } from 'ngx-toastr';
import { RealEstate } from 'src/app/model/myhouse';
import { User } from 'src/app/model/user';
import { MyHouseService } from 'src/app/services/myhouse.service';
import { StoreType } from 'src/app/shared/store/types';
import { RealEstateEntryComponent } from './real-estate-entry/real-estate-entry.component';

@Component({
  selector: 'app-realestates',
  templateUrl: './realestates.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, RealEstateEntryComponent]
})
export class RealEstatesComponent implements OnInit {
  user: User | null = null;
  realEstates: RealEstate[] = [];

  constructor(private houseService: MyHouseService, private store: Store<StoreType>, private toastr: ToastrService) {
    store.subscribe(state => {
      this.user = state.loggedUser.user;
    })
  }

  ngOnInit(): void {
    this.houseService.loadRealEstates(
      this.user!.email,
      (realEstates: RealEstate[]) => {
        console.log(realEstates)
        this.realEstates = realEstates;
      },
      (err) => this.toastr.error(err.message)
    );
  }

}
