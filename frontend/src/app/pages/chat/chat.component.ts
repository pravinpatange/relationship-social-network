import { Component, OnInit, signal, ViewChild, ElementRef, AfterViewChecked, OnDestroy } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ApiService } from "../../core/services/api.service";
import { AuthService } from "../../core/services/auth.service";
import { WebSocketService } from "../../core/services/websocket.service";
import { Router } from "@angular/router";
import { ChatRoom, ChatMessage, Friendship } from "../../core/models";

@Component({
  selector: "app-chat",
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: "./chat.component.html",
  styleUrl: "./chat.component.css"
})
export class ChatComponent implements OnInit, AfterViewChecked, OnDestroy {
  @ViewChild("msgEnd") msgEnd!: ElementRef;

  rooms = signal<ChatRoom[]>([]);
  activeRoom = signal<ChatRoom | null>(null);
  messages = signal<ChatMessage[]>([]);
  newMsg = signal("");
  loading = signal(true);
  sending = signal(false);
  loadingMsgs = signal(false);
  showNewChat = signal(false);
  friendsList = signal<Friendship[]>([]);
  loadingFriends = signal(false);
  initialRoomId: number | null = null;
  private shouldScroll = false;
  private wsSub: any;
  myId = computed(() => this.auth.currentUser()?.id);

  constructor(private api: ApiService, public auth: AuthService, private router: Router, private ws: WebSocketService) {
    const nav = this.router.getCurrentNavigation();
    if (nav?.extras.state?.['roomId']) {
      this.initialRoomId = nav.extras.state['roomId'];
    }
  }

  ngOnInit() {
    this.api.getMyRooms().subscribe({
      next: r => { 
        this.rooms.set(r); 
        this.loading.set(false);
        if (this.initialRoomId) {
          const rm = r.find(x => x.id === this.initialRoomId);
          if (rm) this.selectRoom(rm);
        }
      }, 
      error: () => this.loading.set(false) 
    });

    this.ws.connect();
    this.wsSub = this.ws.messageSubject.subscribe(msg => {
      // If message belongs to active room, append it
      if (this.activeRoom()?.id === msg.roomId) {
        this.messages.update(ms => [...ms, msg]);
        this.shouldScroll = true;
      }
      
      // Update room list order if needed (bump to top)
      const existingRooms = this.rooms();
      const roomIdx = existingRooms.findIndex(r => r.id === msg.roomId);
      if (roomIdx >= 0) {
        const room = existingRooms[roomIdx];
        const newRooms = [...existingRooms];
        newRooms.splice(roomIdx, 1);
        this.rooms.set([room, ...newRooms]);
      }
    });
  }

  ngOnDestroy() {
    if (this.wsSub) this.wsSub.unsubscribe();
    this.ws.disconnect();
  }

  ngAfterViewChecked() {
    if (this.shouldScroll) { this.scrollToBottom(); this.shouldScroll = false; }
  }

  selectRoom(room: ChatRoom) {
    this.activeRoom.set(room);
    this.messages.set([]);
    this.loadingMsgs.set(true);
    this.api.getMessages(room.id).subscribe({ next: m => { this.messages.set(m); this.loadingMsgs.set(false); this.shouldScroll = true; }, error: () => this.loadingMsgs.set(false) });
  }

  toggleNewChat() {
    this.showNewChat.set(!this.showNewChat());
    if (this.showNewChat() && this.friendsList().length === 0) {
      this.loadingFriends.set(true);
      this.api.getFriends().subscribe({
        next: f => { this.friendsList.set(f); this.loadingFriends.set(false); },
        error: () => this.loadingFriends.set(false)
      });
    }
  }

  startChatWithFriend(friend: Friendship) {
    const friendId = friend.requesterId === this.auth.currentUser()?.id ? friend.receiverId : friend.requesterId;
    this.api.startChat(friendId).subscribe(room => {
      let existing = this.rooms().find(r => r.id === room.id);
      if (!existing) {
        this.rooms.update(rs => [room, ...rs]);
      }
      this.selectRoom(room);
      this.showNewChat.set(false);
    });
  }

  send() {
    const text = this.newMsg().trim();
    if (!text || !this.activeRoom()) return;
    this.sending.set(true);
    this.api.sendMessage(this.activeRoom()!.id, text).subscribe(msg => {
      this.messages.update(ms => [...ms, msg]);
      this.newMsg.set(""); this.sending.set(false); this.shouldScroll = true;
    });
  }

  scrollToBottom() {
    try { this.msgEnd.nativeElement.scrollIntoView({ behavior: "smooth" }); } catch {}
  }

  otherUser(room: ChatRoom) {
    return this.myId() === room.user1Id ? room.user2Username : room.user1Username;
  }

  friendName(f: Friendship) {
    return f.requesterId === this.myId() ? f.receiverUsername : f.requesterUsername;
  }

  isMe(msg: ChatMessage) { return msg.senderId === this.myId(); }

  initials(name: string) { return (name||"?").split(" ").map((w:string)=>w[0]).join("").toUpperCase().slice(0,2); }

  timeAgo(d: string) {
    const diff = Date.now() - new Date(d).getTime();
    const m = Math.floor(diff/60000), h = Math.floor(m/60);
    if (h > 0) return h + "h"; if (m > 0) return m + "m"; return "now";
  }

  formatTime(d: string) {
    return new Date(d).toLocaleTimeString([], {hour:"2-digit", minute:"2-digit"});
  }
}