import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faUser,
  faPenToSquare,
  faHouseLaptop,
} from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { RealEstate } from 'src/app/model/myHouse';
import { MyhouseService } from 'src/app/services/myhouse.service';
import { RealEstateEntryComponent } from './real-estate-entry/real-estate-entry.component';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styles: [],
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule,
    FormsModule,
    RealEstateEntryComponent,
  ],
})
export class SettingsComponent implements OnInit {
  faUser: IconDefinition = faUser;

  email: string | null = '';
  realEstates: RealEstate[] | null = null;
  newRealEstate: RealEstate = new RealEstate('','', '', '', []);
  errorEmail: string = '';
  errorName: string = '';
  errorAddress: string = '';
  showAddRealEstate = false;

  constructor(
    private route: ActivatedRoute,
    private houseService: MyhouseService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.email = this.route.snapshot.paramMap.get('email');
  }

  handleShow = () => {
    if (!this.email) {
      this.errorEmail = 'Email is required';
      return;
    } else this.errorEmail = '';
    this.houseService.loadRealEstates(
      this.email!,
      (realEstates: RealEstate[]) => {
        this.realEstates = realEstates;
      },
      (err) => this.toastr.error(err.message)
    );
  };

  handleAdd = () => {
    if (this.valid()) {
      this.houseService.addRealEstates(
        this.email!,
        this.newRealEstate,
        (realEstate: RealEstate) => {
          this.realEstates?.push(realEstate);
        },
        (err) => this.toastr.error(err.message)
      );
    }
  };

  handleShowAdd = () => {
    this.showAddRealEstate = true;
  };

  handleCancel = () => {
    this.newRealEstate = new RealEstate('','', '', '', []);
    this.showAddRealEstate = false;
  };

  valid = () => {
    let valid = true;
    if (!this.newRealEstate.name) {
      this.errorName = 'Name is required';
      valid = false;
    } else this.errorName = '';
    if (!this.newRealEstate.address) {
      this.errorAddress = 'Address is required';
      valid = false;
    } else this.errorAddress = '';
    return valid;
  };
}
