import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition, faHouseLaptop, faKey, faUsers } from '@fortawesome/free-solid-svg-icons';
import { RealEstate } from 'src/app/model/myhouse';
import { TenantsModalComponent } from '../tenants-modal/tenants-modal.component';
import { DevicesModalComponent } from '../devices-modal/devices-modal.component';

@Component({
  selector: 'app-real-estate-entry',
  templateUrl: './real-estate-entry.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, TenantsModalComponent, DevicesModalComponent],
})
export class RealEstateEntryComponent {
  faLandlord: IconDefinition = faKey;
  faTenants: IconDefinition = faUsers;
  faDevices: IconDefinition = faHouseLaptop;

  @Input() realEstate!: RealEstate;
  showTenantsModal: boolean = false;
  showDevicesModal: boolean = false;

  constructor(
    
  ) {}

  ngOnInit(): void {
  }

  openTenantsModal = () => {
    this.showTenantsModal = true;
  }

  openDevicesModal = () => {
   this.showDevicesModal = true;   
  }
}
