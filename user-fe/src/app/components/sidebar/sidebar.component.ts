import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faCheckDouble,
  faHouseUser,
  faRightFromBracket,
  faFileCircleCheck,
  faEnvelope
} from '@fortawesome/free-solid-svg-icons';
import { filter } from 'rxjs';
import { tokenName } from 'src/app/shared/constants';

interface MenuOption {
  title: string;
  link: string;
  icon: IconDefinition;
}

const menus = {
  'User': [
    {
      title: 'My profile',
      link: '',
      icon: faHouseUser,
    },
  ],
  'Other': [
    {
      title: 'Certificate signing request',
      link: 'csr',
      icon: faCheckDouble,
    },
    {
      title: 'Messages',
      link: 'messages',
      icon: faEnvelope,
    },
    {
      title: 'Reports',
      link: 'reports',
      icon: faFileCircleCheck,
    },
  ],
};

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  standalone: true,
  styles: [],
  imports: [CommonModule, FontAwesomeModule]
})
export class SidebarComponent {

  menus: any = menus;
  faRightFromBracket: IconDefinition = faRightFromBracket;
  chosenOption: MenuOption = menus['User'][0];
  objectKeys = Object.keys;

  constructor(private router: Router, private http: HttpClient) {
    router.events
      .pipe(filter((e) => e instanceof NavigationEnd))
      .subscribe((event) => {
        const end = event as NavigationEnd;
        if (end.url.startsWith('')) {
          const menusToLookup: MenuOption[] = [
            ...menus['Other'],
            ...menus['User'],
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
    .subscribe(() => {
      window.location.href = '/';
      window.sessionStorage.removeItem(tokenName);
    });
  }

  navigate(option: MenuOption) {
    this.router.navigate([option.link]);
  }


}
