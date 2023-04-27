import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faLock, faTrash, faUserEdit } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/user.service';
import { YesNoModalComponent } from 'src/app/shared/components/modals/yes-no-modal/yes-no-modal.component';

@Component({
  selector: 'app-user-entry',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, YesNoModalComponent],
  templateUrl: './user-entry.component.html',
  styles: [
  ]
})
export class UserEntryComponent {
  @Input() user!: User;
  faLock: IconDefinition = faLock;
  faTrash: IconDefinition = faTrash;
  faUserEdit: IconDefinition = faUserEdit;
  showDeleteModal: boolean = false;

  constructor(
    private userService: UserService,
    private toastr: ToastrService
  ) { }

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
    location.reload();
  }
  
  closeDeleteModal(): void {
    this.showDeleteModal = false;
  }

  handleChangeRole(): void {
    
  }
}
