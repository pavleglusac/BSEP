import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { ToastrService } from 'ngx-toastr';
import { Device, DeviceType, RealEstate } from 'src/app/model/myhouse';
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
        this.realEstates = realEstates;

        //ovde
        this.realEstates.forEach(re => re.devices = [new Device(DeviceType.MOTION_DETECTOR, "filepath", "duration", "filter regex")])
        this.realEstates.forEach(re => re.devices!.push(new Device(DeviceType.THERMOMETER, "filepath", "duration", "filter regex")))
        this.realEstates.forEach(re => re.devices!.push(new Device(DeviceType.LOCK, "filepath", "duration", "filter regex")))
        this.realEstates.forEach(re => re.devices!.push(new Device(DeviceType.LAMP, "filepath", "duration", "filter regex")))
        this.realEstates.forEach(re => re.devices!.push(new Device(DeviceType.GATE, "filepath", "duration", "filter regex")))
      
      },
      (err) => this.toastr.error(err.message)
    );
  }

}
