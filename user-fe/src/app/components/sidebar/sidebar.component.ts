import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faCheckDouble,
  faHouseUser,
  faRightFromBracket,
  faFileCircleCheck,
  faEnvelope,
  faExclamationCircle
} from '@fortawesome/free-solid-svg-icons';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs';
import { ThreatService } from 'src/app/services/threat.service';
import { tokenName } from 'src/app/shared/constants';
import { AlarmAction, AlarmActionType } from 'src/app/shared/store/threats-slice/threats.actions';
import { StoreType } from 'src/app/shared/store/types';

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
  'Devices' :[
    {
      title: 'Threats',
      link: 'threats',
      icon: faExclamationCircle,
    },
    {
      title: 'Messages',
      link: 'messages',
      icon: faEnvelope,
    },
  ],
  'Other': [
    {
      title: 'Certificate signing request',
      link: 'csr',
      icon: faCheckDouble,
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
export class SidebarComponent implements OnInit {

  menus: any = menus;
  faRightFromBracket: IconDefinition = faRightFromBracket;
  chosenOption: MenuOption = menus['User'][0];
  objectKeys = Object.keys;
  unreadMessages = 0;

  ngOnInit(): void {
    this.fetchAlarams();
    this.fetchMessageAlarms();
  }

  fetchAlarams() {
    this.threatService.getAlarms(
      0,
      (alarmsPage: any) => {
        this.store.dispatch(new AlarmAction(AlarmActionType.ADD_ALARMS, alarmsPage))
        this.store.dispatch(new AlarmAction(AlarmActionType.ADD_UNREAD_MESSAGES_ALARMS, alarmsPage.content.length))
      },
      (err) => {}
    );
  }

  fetchMessageAlarms() {
    this.threatService.getMessageAlarms(
      0,
      (alarmsPage: any) => {
        this.store.dispatch(new AlarmAction(AlarmActionType.ADD_MESSAGES_ALARMS, alarmsPage))
        this.store.dispatch(new AlarmAction(AlarmActionType.ADD_UNREAD_MESSAGES_MESSAGE_ALARMS, alarmsPage.content.length))
      },
      (err) => {}
    );
  }

  constructor(private router: Router, private http: HttpClient, private store: Store<StoreType>, private threatService: ThreatService) {
    store.select('threats').subscribe((alarms) => {
      this.unreadMessages = alarms.unreadMessagesAlarms + alarms.unreadMessagesMessagesAlarms;
    });
    
    router.events
      .pipe(filter((e) => e instanceof NavigationEnd))
      .subscribe((event) => {
        const end = event as NavigationEnd;
        if (end.url.startsWith('')) {
          const menusToLookup: MenuOption[] = [
            ...menus['Other'],
            ...menus['User'],
            ...menus['Devices']
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
