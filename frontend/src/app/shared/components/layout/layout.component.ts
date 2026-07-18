import { Component, signal, OnInit, HostListener } from "@angular/core";
import { RouterOutlet, RouterLink, RouterLinkActive } from "@angular/router";
import { CommonModule } from "@angular/common";
import { AuthService } from "../../../core/services/auth.service";
import { ApiService } from "../../../core/services/api.service";
import { Group, ModeState } from "../../../core/models";

@Component({
  selector: "app-layout",
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: "./layout.component.html",
  styleUrl: "./layout.component.css"
})
export class LayoutComponent implements OnInit {
  sidebarOpen = signal(false);
  unreadCount = signal(0);
  groups = signal<Group[]>([]);
  activeMode = signal<ModeState | null>(null);
  showModeMenu = signal(false);

  readonly navItems = [
    { path: "/feed",          icon: "home",              label: "Feed" },
    { path: "/explore",       icon: "explore",           label: "Explore" },
    { path: "/friends",       icon: "people",            label: "Friends" },
    { path: "/groups",        icon: "hub",               label: "Groups" },
    { path: "/chat",          icon: "chat_bubble_outline",label: "Messages" },
    { path: "/notifications", icon: "notifications_none", label: "Notifications" },
    { path: "/profile",       icon: "person_outline",    label: "Profile" },
  ];

  constructor(public auth: AuthService, private api: ApiService) {}

  ngOnInit() {
    this.api.getUnreadCount().subscribe(r => this.unreadCount.set(r.unreadCount));
    this.api.getGroups().subscribe(g => this.groups.set(g));
    this.api.getCurrentMode().subscribe(m => this.activeMode.set(m));
    setInterval(() => this.api.getUnreadCount().subscribe(r => this.unreadCount.set(r.unreadCount)), 30000);
  }

  switchMode(groupId: number | null) {
    this.api.changeMode(groupId).subscribe(m => { this.activeMode.set(m); this.showModeMenu.set(false); });
  }

  get currentUser() { return this.auth.currentUser(); }

  initials(name?: string) {
    if (!name) return "?";
    return name.split(" ").map(w => w[0]).join("").toUpperCase().slice(0,2);
  }

  @HostListener("document:click", ["$event"])
  onDocClick(e: MouseEvent) {
    const t = e.target as HTMLElement;
    if (!t.closest(".mode-menu-wrap")) this.showModeMenu.set(false);
  }
}