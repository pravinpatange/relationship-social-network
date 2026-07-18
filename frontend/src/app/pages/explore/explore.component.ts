import { Component, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { RouterLink } from "@angular/router";
import { ApiService } from "../../core/services/api.service";
import { User, Post } from "../../core/models";
import { PostCardComponent } from "../../shared/components/post-card/post-card.component";

@Component({
  selector: "app-explore",
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, PostCardComponent],
  templateUrl: "./explore.component.html",
  styleUrl: "./explore.component.css"
})
export class ExploreComponent {
  searchQuery = signal("");
  userResults = signal<(User & { _sent?: boolean })[]>([]);
  publicPosts = signal<Post[]>([]);
  searching = signal(false);
  loadingPosts = signal(true);
  activeTab = signal<"people"|"posts">("people");

  constructor(private api: ApiService) {
    this.api.getFeed(undefined, 0, 20).subscribe({
      next: r => { this.publicPosts.set(r.content); this.loadingPosts.set(false); },
      error: () => this.loadingPosts.set(false)
    });
  }

  search() {
    const q = this.searchQuery().trim();
    if (q.length < 2) return;
    this.searching.set(true);
    this.api.searchUsers(q).subscribe({
      next: r => { this.userResults.set(r.content); this.searching.set(false); },
      error: () => this.searching.set(false)
    });
  }

  sendRequest(userId: number) {
    this.api.sendFriendRequest(userId).subscribe({
      next: () => {
        this.userResults.update(rs => rs.map(u => u.id === userId ? {...u, _sent: true} as any : u));
      },
      error: (err) => {
        if (err.error?.message === 'Friend request already sent') {
          this.userResults.update(rs => rs.map(u => u.id === userId ? {...u, _sent: true} as any : u));
        }
      }
    });
  }

  initials(name: string) { return (name||"?").split(" ").map((w:string)=>w[0]).join("").toUpperCase().slice(0,2); }
}