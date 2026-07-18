import { Component, OnInit, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { RouterLink, Router } from "@angular/router";
import { ApiService } from "../../core/services/api.service";
import { AuthService } from "../../core/services/auth.service";
import { Friendship, User, Group } from "../../core/models";

type Tab = "friends" | "requests" | "search";
type SearchUser = User & { _requested?: boolean; _isFriend?: boolean; _pending?: boolean };

@Component({
  selector: "app-friends",
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: "./friends.component.html",
  styleUrl: "./friends.component.css"
})
export class FriendsComponent implements OnInit {
  tab = signal<Tab>("friends");
  friends = signal<Friendship[]>([]);
  pending = signal<Friendship[]>([]);
  sent = signal<Friendship[]>([]);
  searchResults = signal<SearchUser[]>([]);
  suggestions = signal<SearchUser[]>([]);
  searchQuery = signal("");
  loading = signal(true);
  searching = signal(false);
  groups = signal<Group[]>([]);
  assignTarget = signal<Friendship | null>(null);
  assignedGroups = signal<number[]>([]);
  savingGroups = signal(false);
  myId = computed(() => this.auth.currentUser()?.id);

  constructor(private api: ApiService, private router: Router, private auth: AuthService) {}

  ngOnInit() {
    this.loadFriends();
    this.api.getPendingRequests().subscribe(r => this.pending.set(r));
    this.api.getSentRequests().subscribe(r => this.sent.set(r));
    this.api.getGroups().subscribe(g => this.groups.set(g));
    this.api.getSuggestions().subscribe(s => this.suggestions.set(this.mapUsers(s)));
  }

  loadFriends() {
    this.loading.set(true);
    this.api.getFriends().subscribe({ next: f => { this.friends.set(f); this.loading.set(false); }, error: () => this.loading.set(false) });
  }

  search() {
    const q = this.searchQuery().trim();
    if (q.length < 2) return;
    this.searching.set(true);
    this.api.searchUsers(q).subscribe({ next: r => { this.searchResults.set(this.mapUsers(r.content)); this.searching.set(false); }, error: () => this.searching.set(false) });
  }

  private mapUsers(users: User[]): SearchUser[] {
    return users.map(u => ({
      ...u,
      _requested: this.sent().some(f => f.receiverId === u.id),
      _isFriend: this.friends().some(f => f.requesterId === u.id || f.receiverId === u.id),
      _pending: this.pending().some(f => f.requesterId === u.id)
    }));
  }

  sendRequest(userId: number) {
    this.api.sendFriendRequest(userId).subscribe({
      next: () => {
        this.searchResults.update(rs => rs.map(u => u.id === userId ? {...u, _requested: true} as any : u));
        this.suggestions.update(rs => rs.map(u => u.id === userId ? {...u, _requested: true} as any : u));
      },
      error: (err) => {
        if (err.error?.message === 'Friend request already sent') {
          this.searchResults.update(rs => rs.map(u => u.id === userId ? {...u, _requested: true} as any : u));
          this.suggestions.update(rs => rs.map(u => u.id === userId ? {...u, _requested: true} as any : u));
        }
      }
    });
  }

  accept(id: number) {
    this.api.acceptRequest(id).subscribe({
      next: () => {
        this.pending.update(ps => ps.filter(p => p.id !== id));
        this.loadFriends();
      },
      error: () => {
        // If it fails (e.g. already accepted), refresh lists
        this.api.getPendingRequests().subscribe(r => this.pending.set(r));
        this.loadFriends();
      }
    });
  }

  reject(id: number) {
    this.api.rejectRequest(id).subscribe({
      next: () => this.pending.update(ps => ps.filter(p => p.id !== id)),
      error: () => this.api.getPendingRequests().subscribe(r => this.pending.set(r))
    });
  }

  unfriend(friendId: number) {
    this.api.unfriend(friendId).subscribe({
      next: () => this.friends.update(fs => fs.filter(f => f.requesterId !== friendId && f.receiverId !== friendId)),
      error: (err) => console.error("Failed to unfriend:", err)
    });
  }

  openAssign(f: Friendship) {
    this.assignTarget.set(f);
    const fid = f.requesterId === this.currentUserId() ? f.receiverId : f.requesterId;
    this.api.getFriendGroups(fid).subscribe(gs => this.assignedGroups.set(gs.map(g => g.groupId)));
  }

  toggleAssignGroup(id: number) {
    this.assignedGroups.update(gs => gs.includes(id) ? gs.filter(g => g !== id) : [...gs, id]);
  }

  saveGroups() {
    const f = this.assignTarget()!;
    const fid = f.requesterId === this.currentUserId() ? f.receiverId : f.requesterId;
    this.savingGroups.set(true);
    this.api.assignGroups(fid, this.assignedGroups()).subscribe(() => { this.savingGroups.set(false); this.assignTarget.set(null); });
  }

  currentUserId() {
    return this.myId();
  }

  messageFriend(f: Friendship) {
    const friendId = f.requesterId === this.myId() ? f.receiverId : f.requesterId;
    this.api.startChat(friendId).subscribe(room => {
      this.router.navigate(['/chat'], { state: { roomId: room.id } });
    });
  }

  friendId(f: Friendship) {
    return f.requesterId === this.myId() ? f.receiverId : f.requesterId;
  }

  friendName(f: Friendship) {
    return f.requesterId === this.myId() ? f.receiverUsername : f.requesterUsername;
  }

  initials(name: string) { return name.split(" ").map(w=>w[0]).join("").toUpperCase().slice(0,2); }
}