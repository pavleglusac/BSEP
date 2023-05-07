import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faLock,
  faTrash,
  faUserEdit,
  faHouseMedical,
  faArrowDownUpAcrossLine,
} from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/user.service';
import { YesNoModalComponent } from 'src/app/shared/components/modals/yes-no-modal/yes-no-modal.component';

@Component({
  selector: 'app-user-entry',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, YesNoModalComponent],
  templateUrl: './user-entry.component.html',
  styles: [],
})
export class UserEntryComponent {
  @Input() user!: User;
  faLock: IconDefinition = faLock;
  faTrash: IconDefinition = faTrash;
  faUserEdit: IconDefinition = faUserEdit;
  faHouse: IconDefinition = faHouseMedical;
  showDeleteModal: boolean = false;
  showRoleChangeModal: boolean = false;
  roleChangeModalDescription: string = '';

  constructor(
    private userService: UserService,
    private toastr: ToastrService,
    private router: Router
  ) {}
  

  handleDelete(): void {
    this.showDeleteModal = true;
  }

  sendDeletionRequest(): void {
    this.userService.delete(
      this.user.id,
      (message: string) => {
        this.toastr.success(message);
      },
      (err) => this.toastr.error(err.message));
    this.closeDeleteModal();
    setTimeout(() => {
      location.reload();
    }, 500)
  }
  
  closeDeleteModal(): void {
    this.showDeleteModal = false;
  }

  closeRoleChangeModal(): void {
    this.showRoleChangeModal = false; 
  }
  
  handleRealEstate(): void {
    this.router.navigate([`admin/settings/${this.user!.email}`]);
  }

  handleRoleChange(): void {
    this.showRoleChangeModal = true;
    this.roleChangeModalDescription = `Are you sure you want to change this user\'s role to ${this.user.role === 'ROLE_TENANT' ? 'LANDLORD' : 'TENANT'}? You will lose all your real estates.`;
  }

  sendRoleChangeRequest(): void {
    this.userService.changeRole(
      this.user.id,
      this.user.role === 'ROLE_TENANT' ? 'ROLE_LANDLORD' : 'ROLE_TENANT',
      (message: string) => {
        this.toastr.success(message);
      },
      (err) => this.toastr.error(err.message));
    this.closeRoleChangeModal();
    setTimeout(() => {
      location.reload();
    }, 500)
  }
}
