import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faCheckDouble, faCertificate, faTurnDown, faMagnifyingGlass, faArrowsDownToPeople, faUserPlus, faUserGroup, faGear, faClipboard, faCircleExclamation, faRightFromBracket, faFileLines } from '@fortawesome/free-solid-svg-icons';

interface MenuOption {
  title: string;
  link: string;
  icon: IconDefinition;
}

const menus = {
  'Public Keys': [
    {
      title: 'Review requests',
      link: 'admin/',
      icon: faCheckDouble,
    },
    {
      title: 'Create certificate',
      link: 'admin/create-certificate',
      icon: faCertificate,
    },
    {
      title: 'All certificates',
      link: 'admin/certificates',
      icon: faFileLines,
    }
  ],
  'Users': [
    {
      title: 'Register',
      link: '',
      icon: faUserPlus,
    },
    {
      title: 'All users',
      link: '',
      icon: faUserGroup,
    },
  ],
  'System': [
    {
      title: 'Settings',
      link: '',
      icon: faGear,
    },
    {
      title: 'Logs',
      link: '',
      icon: faClipboard,
    },
    {
      title: 'Alarms',
      link: '',
      icon: faCircleExclamation,
    },
  ]
}

@Component({
  selector: 'app-admin-sidebar',
  templateUrl: './admin-sidebar.component.html',
  standalone: true,
  styles: [
  ],
  imports: [ CommonModule, FontAwesomeModule ]
})
export class AdminSidebarComponent {
  menus: any = menus;
  faRightFromBracket: IconDefinition = faRightFromBracket;
  chosenOption: MenuOption = menus['Public Keys'][0];
  objectKeys = Object.keys;

  constructor(private router: Router) { }

  signOut(): void {
    // TODO: Implement method
  }

  navigate(option: MenuOption) {
    this.router.navigate([option.link]);
  } 
}
