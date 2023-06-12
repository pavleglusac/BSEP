import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faCheckDouble,
  faCertificate,
  faUserPlus,
  faUserGroup,
  faGear,
  faClipboard,
  faCircleExclamation,
  faRightFromBracket,
  faFileLines,
  faRectangleList,
} from '@fortawesome/free-solid-svg-icons';
import { filter } from 'rxjs';
import { environment } from 'src/environment/environment';

interface MenuOption {
  title: string;
  link: string;
  icon: IconDefinition;
}

const menus = {
  'Public Keys': [
    {
      title: 'Review requests',
      link: 'admin/requests',
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
    },
  ],
  Users: [
    {
      title: 'Register',
      link: 'admin/register',
      icon: faUserPlus,
    },
    {
      title: 'Users',
      link: 'admin/users',
      icon: faUserGroup,
    },
  ],
  System: [
    {
      title: 'Settings',
      link: 'admin/settings',
      icon: faGear,
    },
    {
      title: 'Logs',
      link: 'admin/',
      icon: faClipboard,
    },
    {
      title: 'Log Rules',
      link: 'admin/log-rules',
      icon: faRectangleList,
    },
    {
      title: 'Alarms',
      link: 'admin/',
      icon: faCircleExclamation,
    },
    {
      title: 'Alarm Rules',
      link: 'admin/alarm-rules',
      icon: faRectangleList,
    },
  ],
};

@Component({
  selector: 'app-admin-sidebar',
  templateUrl: './admin-sidebar.component.html',
  standalone: true,
  styles: [],
  imports: [CommonModule, FontAwesomeModule],
})
export class AdminSidebarComponent {
  menus: any = menus;
  faRightFromBracket: IconDefinition = faRightFromBracket;
  chosenOption: MenuOption = menus['Public Keys'][0];
  objectKeys = Object.keys;

  constructor(private router: Router, private http: HttpClient) {
    router.events
      .pipe(filter((e) => e instanceof NavigationEnd))
      .subscribe((event) => {
        const end = event as NavigationEnd;
        if (end.url.startsWith('/admin')) {
          const menusToLookup: MenuOption[] = [
            ...menus['Public Keys'],
            ...menus.System,
            ...menus.Users,
          ];
          this.chosenOption = menusToLookup.find(
            (option: MenuOption) => `/${option.link}` === end.url
          )!;
        }
      });
  }

  signOut(): void {
    // accept plain text response
    this.http
    .post('api/auth/logout', {}, { responseType: 'text' })
    .subscribe({
      next() {
        window.location.href = '/';
      },
      error(error) {
        window.location.href = '/';
      }
    });
    
    window.sessionStorage.removeItem(environment.tokenName);
  }

  navigate(option: MenuOption) {
    this.router.navigate([option.link]);
  }
}
