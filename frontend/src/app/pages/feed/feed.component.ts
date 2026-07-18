import { Component, OnInit, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ApiService } from "../../core/services/api.service";
import { AuthService } from "../../core/services/auth.service";
import { Post, Group, ModeState } from "../../core/models";
import { PostCardComponent } from "../../shared/components/post-card/post-card.component";
import { CreatePostComponent } from "../../shared/components/create-post/create-post.component";

@Component({
  selector: "app-feed",
  standalone: true,
  imports: [CommonModule, PostCardComponent, CreatePostComponent],
  templateUrl: "./feed.component.html",
  styleUrl: "./feed.component.css"
})
export class FeedComponent implements OnInit {
  posts = signal<Post[]>([]);
  loading = signal(true);
  loadingMore = signal(false);
  page = signal(0);
  hasMore = signal(true);
  groups = signal<Group[]>([]);
  activeMode = signal<ModeState | null>(null);
  activeGroupId = signal<number | null>(null);

  constructor(private api: ApiService, public auth: AuthService) {}

  ngOnInit() {
    this.api.getGroups().subscribe(g => this.groups.set(g));
    this.api.getCurrentMode().subscribe(m => {
      this.activeMode.set(m);
      this.activeGroupId.set(m.activeGroupId ?? null);
      this.loadFeed(true);
    });
  }

  loadFeed(reset = false) {
    if (reset) { this.page.set(0); this.posts.set([]); this.hasMore.set(true); }
    this.loading.set(reset);
    this.loadingMore.set(!reset);
    this.api.getFeed(this.activeGroupId() ?? undefined, this.page(), 10).subscribe({
      next: r => {
        this.posts.update(arr => [...arr, ...r.content]);
        this.hasMore.set(!r.last);
        this.loading.set(false);
        this.loadingMore.set(false);
      },
      error: () => { this.loading.set(false); this.loadingMore.set(false); }
    });
  }

  loadMore() {
    if (!this.hasMore() || this.loadingMore()) return;
    this.page.update(p => p + 1);
    this.loadFeed(false);
  }

  switchGroup(groupId: number | null) {
    this.activeGroupId.set(groupId);
    this.api.changeMode(groupId).subscribe(() => this.loadFeed(true));
  }

  onNewPost(post: Post) { this.posts.update(arr => [post, ...arr]); }
  onPostDeleted(id: number) { this.posts.update(arr => arr.filter(p => p.id !== id)); }
}