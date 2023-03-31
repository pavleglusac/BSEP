import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminSidebarComponent } from './admin-sidebar/admin-sidebar.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, AdminSidebarComponent, RouterModule],
  templateUrl: './admin.component.html',
  styles: [
  ]
})
export class AdminComponent {

}
