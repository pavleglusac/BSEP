import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition, faKey, faUsers } from '@fortawesome/free-solid-svg-icons';
import { RealEstate } from 'src/app/model/myhouse';
import { TenantsModalComponent } from '../tenants-modal/tenants-modal.component';

@Component({
  selector: 'app-real-estate-entry',
  templateUrl: './real-estate-entry.component.html',
  styleUrls: [],
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, TenantsModalComponent],
})
export class RealEstateEntryComponent {
  faLandlord: IconDefinition = faKey;
  faTenants: IconDefinition = faUsers;

  @Input() realEstate!: RealEstate;
  showTenantsModal: boolean = false;

  constructor(
    
  ) {}

  ngOnInit(): void {
  }

  openTenantsModal = () => {
    this.showTenantsModal = true;
  }
  
}
