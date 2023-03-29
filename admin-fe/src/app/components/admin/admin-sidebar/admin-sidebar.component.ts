import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import { faCheckDouble, faCertificate, faTurnDown, faMagnifyingGlass, faArrowsDownToPeople, faUserPlus, faUserGroup, faGear, faClipboard, faCircleExclamation, faRightFromBracket } from '@fortawesome/free-solid-svg-icons';

interface MenuOption {
  title: string;
  link: string;
  icon: IconDefinition;
}

const menus = {
  'Public Keys': [
    {
      title: 'Review requests',
      link: '',
      icon: faCheckDouble,
    },
    {
      title: 'Create certificate',
      link: '',
      icon: faCertificate,
    },
    {
      title: 'Withdraw certificate',
      link: '',
      icon: faTurnDown,
    },
    {
      title: 'Insights',
      link: '',
      icon: faMagnifyingGlass,
    },
    {
      title: 'Distribute',
      link: '',
      icon: faArrowsDownToPeople,
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

  signOut(): void {
    // TODO: Implement method
  }
}
