import { Component } from '@angular/core';
import { WebSocketService } from './services/websocket.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent {
  title = 'admin-fe';

  constructor(private webSocketService: WebSocketService) { }

  ngOnInit() {
    this.webSocketService.connect();
  }

  ngOnDestroy() {
    this.webSocketService.disconnect();
  }

}
