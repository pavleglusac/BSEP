import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faHouseLaptop,
  faPenToSquare,
  faCheck,
  faKey,
  faUsers
} from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { RealEstate } from 'src/app/model/myHouse';
import { MyhouseService } from 'src/app/services/myhouse.service';
import { EditTenantsComponent } from '../edit-tenants/edit-tenants.component';

@Component({
  selector: 'app-real-estate-entry',
  templateUrl: './real-estate-entry.component.html',
  styleUrls: ['./real-estate-entry.css'],
  standalone: true,
  imports: [FontAwesomeModule, FormsModule, CommonModule, EditTenantsComponent],
})
export class RealEstateEntryComponent implements OnInit {
  faEdit: IconDefinition = faPenToSquare;
  faDevices: IconDefinition = faHouseLaptop;
  faSave: IconDefinition = faCheck;
  faLandlord: IconDefinition = faKey;
  faTenants: IconDefinition = faUsers;

  @Input() realEstate!: RealEstate;
  @Input() email!: string;
  editable: boolean = false;
  showEditTenantsModal: boolean = false;
  editableRealEstate: RealEstate = new RealEstate('','', '', '', []);

  constructor(
    private houseService: MyhouseService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.editableRealEstate = {...this.realEstate};
  }

  edit = () => {
    this.editable = true;
  };

  save = () => {
    this.saveRealEstate();
    this.editable = false;
  };

  saveRealEstate = () => {
    if (this.valid()) {
      this.realEstate.name = this.editableRealEstate.name;
      this.realEstate.address = this.editableRealEstate.address;
      this.houseService.editRealEstates(
        this.email,
        this.realEstate,
        (realEstate: RealEstate) => {
          this.realEstate = realEstate;
        },
        (err) => this.toastr.error(err.message)
      );
    } else {
      this.editableRealEstate.name = this.realEstate.name;
      this.editableRealEstate.address = this.realEstate.address;
      this.toastr.error('Please fill all fields');
    }
  };

  valid = () => {
    return this.email && this.editableRealEstate.name && this.editableRealEstate.address;
  };

  handleEditTenants = () => {
    this.showEditTenantsModal = true;
  }

  addNewTenant = (tenant: any) => {
    if (this.realEstate.tenants.filter((t) => t.email === tenant.email).length === 0) {
      this.realEstate.tenants.push(tenant);
    }
  }
}
