import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { DialogModule } from 'primeng/dialog';
import { Tenant } from 'src/app/model/user';
import { IconDefinition, faPlus } from '@fortawesome/free-solid-svg-icons';
import { MyhouseService } from 'src/app/services/myhouse.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-edit-tenants',
  templateUrl: './edit-tenants.component.html',
  styles: [
  ],
  standalone: true,
  imports: [CommonModule, DialogModule, FormsModule, FontAwesomeModule]
})
export class EditTenantsComponent {

  constructor(private houseService: MyhouseService, private toastr: ToastrService) { }

  change() {
    this.visibleChange.emit(this.visible);
  }

  @Input() visible: boolean = false;

  @Output() visibleChange = new EventEmitter<boolean>();

  @Input() tenants: Tenant[] | undefined = undefined;

  @Output() newTenantAdded = new EventEmitter<Tenant>();

  @Input() realEstateId: string | undefined = undefined;


  faAdd: IconDefinition = faPlus;

  newTenantEmail: string = '';

  hideDialog() {
    this.visible = false;
    this.change();
  }

  onAdd = () => {
    if (this.newTenantEmail) {
      this.houseService.addNewTenant(this.newTenantEmail, this.realEstateId!, 
      (tenant: Tenant) => {
        this.newTenantAdded.emit(tenant);
        this.newTenantEmail = '';
      }, 
      (error: any) => {this.toastr.error(error.message);});
    } else {
      this.toastr.error('Please enter a valid email address');
    }
  }
}
