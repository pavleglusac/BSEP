import { Component } from '@angular/core';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CsrComponent } from '../csr/csr.component';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: [],
  standalone: true,
  imports: [SidebarComponent, CsrComponent, CommonModule, RouterModule]
})
export class HomepageComponent {
  constructor() {}
}
