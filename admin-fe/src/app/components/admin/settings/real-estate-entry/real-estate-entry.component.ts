import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faHouseLaptop,
  faPenToSquare,
  faCheck,
} from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { RealEstate } from 'src/app/model/myHouse';
import { MyhouseService } from 'src/app/services/myhouse.service';

@Component({
  selector: 'app-real-estate-entry',
  templateUrl: './real-estate-entry.component.html',
  styleUrls: ['./real-estate-entry.css'],
  standalone: true,
  imports: [FontAwesomeModule, FormsModule, CommonModule],
})
export class RealEstateEntryComponent {
  faEdit: IconDefinition = faPenToSquare;
  faDevices: IconDefinition = faHouseLaptop;
  faSave: IconDefinition = faCheck;

  @Input() realEstate!: RealEstate;
  @Input() email!: string;
  editable: boolean = false;

  constructor(
    private houseService: MyhouseService,
    private toastr: ToastrService
  ) {}

  edit = () => {
    this.editable = true;
  };

  save = () => {
    this.saveRealEstate();
    this.editable = false;
  };

  saveRealEstate = () => {
    if (this.valid()) {
      this.houseService.editRealEstates(
        this.email,
        this.realEstate,
        (realEstate: RealEstate) => {
          this.realEstate = realEstate;
        },
        (err) => this.toastr.error(err.message)
      );
    }
  };

  valid = () => {
    return true;
  };
}
