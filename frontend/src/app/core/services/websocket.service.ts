import { Injectable } from "@angular/core";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { Subject } from "rxjs";
import { ChatMessage } from "../models";
import { AuthService } from "./auth.service";

@Injectable({ providedIn: 'root' })
export class WebSocketService {
  private client: Client;
  public messageSubject = new Subject<ChatMessage>();

  constructor(private authService: AuthService) {
    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws-sockjs'),
      connectHeaders: {},
      debug: (str) => { console.log("[STOMP]", str); },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    this.client.onConnect = (frame) => {
      this.client.subscribe('/user/queue/messages', (message) => {
        if (message.body) {
          const chatMsg = JSON.parse(message.body) as ChatMessage;
          this.messageSubject.next(chatMsg);
        }
      });
    };
  }

  public connect() {
    const token = this.authService.token();
    if (token) {
      this.client.connectHeaders = { Authorization: 'Bearer ' + token };
      if (!this.client.active) this.client.activate();
    }
  }

  public disconnect() {
    if (this.client.active) this.client.deactivate();
  }
}
