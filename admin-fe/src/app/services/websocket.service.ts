import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import { ToastrService } from 'ngx-toastr';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/environment/environment';
import { StoreType } from '../shared/store/types';
import { Store } from '@ngrx/store';
import { AlarmAction, AlarmActionType } from '../shared/store/alarms-slice/alarms.actions';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private client: Client;

  constructor(private toastr: ToastrService, private store: Store<StoreType>) {
    const token = sessionStorage.getItem(environment.tokenName);
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
    // TODO: stize alarm ondosno logalarm za svaki endpoint, dodati toster i to sve
    this.client.subscribe('/user/queue/logs', (msg: any) => {
      let body = JSON.parse(msg.body);
      console.log(body)
      this.toastr.warning("NEW LOG ALARM!");
      this.store.dispatch(new AlarmAction(AlarmActionType.ADD_UNREAD_LOG_ALARMS, 1));
      if (window.location.pathname === '/admin/log-alarms') {
        setTimeout(() => {
          window.location.reload();
          window.location.href = '/admin/log-alarms';
        }, 1250)
      }
    });

    this.client.subscribe('/user/queue/alarms', (msg: any) => {
      let body = JSON.parse(msg.body);
      console.log(body);
      this.toastr.warning("NEW ALARM!");
      this.store.dispatch(new AlarmAction(AlarmActionType.ADD_UNREAD_ALARMS, 1));
      if (window.location.pathname === '/admin/alarms') {
        setTimeout(() => {
          window.location.reload();
          window.location.href = '/admin/alarms';
        }, 1250)
      }
    });
  }
}