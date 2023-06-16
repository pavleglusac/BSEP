import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { tokenName } from './../shared/constants';
import { StoreType } from '../shared/store/types';
import { Store } from '@ngrx/store';
import { AlarmAction, AlarmActionType } from '../shared/store/threats-slice/threats.actions';
import { ThreatService } from './threat.service';


@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private client: Client;
  private pageAlarms: number = 0;

  constructor(private store: Store<StoreType>, private threatService: ThreatService) {
    this.store.subscribe((state) => {
      console.log(state.threats.pageInfoAlarms)
      this.pageAlarms = state.threats.pageInfoAlarms?.number;
    });
    const token = sessionStorage.getItem(tokenName);
    this.client = new Client({
      webSocketFactory: () => new SockJS('https://localhost:8080/ws?token=' + token),
    });
  }

  connect() {
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
    this.client.publish({
      destination: '/app/hello',
      body: message
    });
  }

  subscribeToTopic() {

    this.client.subscribe('/user/queue/alarms', (msg) => {
        console.log(msg);
        // json parse
        let alarm = JSON.parse(msg.body);
        this.fetchAlarams();
        
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
}