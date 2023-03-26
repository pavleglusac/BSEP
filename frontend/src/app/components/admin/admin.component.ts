import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminSidebarComponent } from './admin-sidebar/admin-sidebar.component';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, AdminSidebarComponent],
  templateUrl: './admin.component.html',
  styles: [
  ]
})
export class AdminComponent {

}
