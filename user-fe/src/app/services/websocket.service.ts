import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { tokenName } from './../shared/constants';
import { StoreType } from '../shared/store/types';
import { Store } from '@ngrx/store';
import { AlarmAction, AlarmActionType } from '../shared/store/threats-slice/threats.actions';
import { ThreatService } from './threat.service';
import { User } from '../model/user';


@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private client: Client | null = null;;
  private pageAlarms: number = 0;
  private pageMsgs: number = 0;
  private user: User | null = null;

  constructor(private store: Store<StoreType>, private threatService: ThreatService) {
    this.store.subscribe((state) => {
      this.pageAlarms = state.threats.pageInfoAlarms?.number;
      this.pageMsgs = state.threats.pageInfoMessagesAlarms?.number;
      if (state.loggedUser.user != null && state.loggedUser.user.id != this.user?.id) {
        this.user = state.loggedUser.user;
        const token = sessionStorage.getItem(tokenName);
        this.client = new Client({
          webSocketFactory: () => new SockJS('https://localhost:8080/ws?token=' + token),
        });
        this.connect();
      }
    });
 
  }

  connect() {
    if (!this.client) return;
    this.client.onConnect = (frame: any) => {
      console.log('Connected: ' + frame);
      // subscribe to a topic or do something else
      this.subscribeToTopic();
    };

    this.client.onStompError = (frame: any) => {
      console.log('Broker reported error: ' + frame.headers['message']);
      console.log('Additional details: ' + frame.body);
    };

    this.client.activate();

  }

  disconnect() {
    if (this.client) {
      this.client.deactivate();
    }
  }

  sendMessage(message: string) {
    if (!this.client) return;
    this.client.publish({
      destination: '/app/hello',
      body: message
    });
  }

  subscribeToTopic() {
    if (!this.client) return;

    this.client.subscribe('/user/queue/alarms', (msg) => {
        // json parse
        let alarm = JSON.parse(msg.body);
        this.fetchAlarams();
        
    });

    this.client.subscribe('/user/queue/message', (msg) => {
      console.log(msg);
      // json parse
      let msgAlarm = JSON.parse(msg.body);
      this.fetchMessageAlarms();
  });
  }

  fetchAlarams() {
    this.threatService.getAlarms(
      this.pageAlarms,
      (alarmsPage: any) => {
        this.store.dispatch(new AlarmAction(AlarmActionType.ADD_ALARMS, alarmsPage))
        this.store.dispatch(new AlarmAction(AlarmActionType.ADD_UNREAD_MESSAGES_ALARMS, 1))
      },
      (err) => {}
    );
  }

  fetchMessageAlarms() {
    this.threatService.getMessageAlarms(
      this.pageMsgs,
      (alarmsPage: any) => {
        this.store.dispatch(new AlarmAction(AlarmActionType.ADD_MESSAGES_ALARMS, alarmsPage))
        this.store.dispatch(new AlarmAction(AlarmActionType.ADD_UNREAD_MESSAGES_MESSAGE_ALARMS, 1))
      },
      (err) => {}
    );
  }
}