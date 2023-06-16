import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private client: Client;

  constructor() {
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
    this.client.subscribe('/user/queue/logs', (msg) => {
      let body = JSON.parse(msg.body);
      console.log(body)
    });

    this.client.subscribe('/user/queue/alarms', (msg) => {
      let body = JSON.parse(msg.body);
      console.log(body);
    });
  }
}