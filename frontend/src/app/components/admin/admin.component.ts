import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule],
  template: `
    <p class="text-center text-2xl font-medium">
      admin works!
    </p>
  `,
  styles: [
  ]
})
export class AdminComponent {

}
