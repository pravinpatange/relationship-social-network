import { Component, OnInit, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ApiService } from "../../core/services/api.service";
import { Notification } from "../../core/models";

@Component({
  selector: "app-notifications",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./notifications.component.html",
  styleUrl: "./notifications.component.css"
})
export class NotificationsComponent implements OnInit {
  notifications = signal<Notification[]>([]);
  loading = signal(true);
  unread = signal(0);

  constructor(private api: ApiService) { }

  ngOnInit() {
    this.api.getNotifications().subscribe({ next: n => { this.notifications.set(n); this.unread.set(n.filter(x => !x.read).length); this.loading.set(false); }, error: () => this.loading.set(false) });
  }

  markAll() {
    this.api.markAllRead().subscribe(() => { this.notifications.update(ns => ns.map(n => ({ ...n, read: true }))); this.unread.set(0); });
  }

  markOne(id: number) {
    this.api.markOneRead(id).subscribe(() => { this.notifications.update(ns => ns.map(n => n.id === id ? { ...n, read: true } : n)); this.unread.update(u => Math.max(0, u - 1)); });
  }

  icon(type: string) {
    const map: any = { FRIEND_REQUEST: "person_add", FRIEND_ACCEPTED: "how_to_reg", POST_LIKE: "favorite", POST_COMMENT: "chat_bubble", FOLLOW: "rss_feed" };
    return map[type] || "notifications";
  }

  iconColor(type: string) {
    const map: any = { FRIEND_REQUEST: "violet", FRIEND_ACCEPTED: "teal", POST_LIKE: "coral", POST_COMMENT: "blue", FOLLOW: "violet" };
    return map[type] || "violet";
  }

  message(n: Notification) {
    const actor = n.actorUsername || "Someone";
    const map: any = { FRIEND_REQUEST: `${actor} sent you a friend request`, FRIEND_ACCEPTED: `${actor} accepted your friend request`, POST_LIKE: `${actor} liked your post`, POST_COMMENT: `${actor} commented on your post`, FOLLOW: `${actor} started following you` };
    return map[n.type] || "New notification";
  }

  timeAgo(d: string) {
    const diff = Date.now() - new Date(d).getTime();
    const m = Math.floor(diff / 60000), h = Math.floor(m / 60), day = Math.floor(h / 24);
    if (day > 0) return `${day}d ago`; if (h > 0) return `${h}h ago`; if (m > 0) return `${m}m ago`; return "Just now";
  }
}