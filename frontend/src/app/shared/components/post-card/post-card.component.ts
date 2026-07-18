import { Component, Input, Output, EventEmitter, signal, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { FormsModule } from "@angular/forms";
import { Post, Comment } from "../../../core/models";
import { ApiService } from "../../../core/services/api.service";
import { AuthService } from "../../../core/services/auth.service";

@Component({
  selector: "app-post-card",
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: "./post-card.component.html",
  styleUrl: "./post-card.component.css"
})
export class PostCardComponent implements OnInit {
  @Input() post!: Post;
  @Output() deleted = new EventEmitter<number>();

  liked = signal(false);
  likeCount = signal(0);
  showComments = signal(false);
  comments = signal<Comment[]>([]);
  newComment = signal("");
  loadingComment = signal(false);
  menuOpen = signal(false);

  constructor(private api: ApiService, public auth: AuthService) {}

  ngOnInit() {
    this.likeCount.set(this.post.likeCount);
    this.api.getLikeStatus(this.post.id).subscribe(s => {
      this.liked.set(s.likedByMe);
      this.likeCount.set(s.likeCount);
    });
  }

  toggleLike() {
    if (this.liked()) {
      this.api.unlikePost(this.post.id).subscribe(s => { this.liked.set(false); this.likeCount.set(s.likeCount); });
    } else {
      this.api.likePost(this.post.id).subscribe(s => { this.liked.set(true); this.likeCount.set(s.likeCount); });
    }
  }

  toggleComments() {
    this.showComments.set(!this.showComments());
    if (this.showComments() && this.comments().length === 0) {
      this.api.getComments(this.post.id).subscribe(c => this.comments.set(c));
    }
  }

  submitComment() {
    const text = this.newComment().trim();
    if (!text) return;
    this.loadingComment.set(true);
    this.api.addComment(this.post.id, text).subscribe(c => {
      this.comments.update(arr => [...arr, c]);
      this.newComment.set("");
      this.loadingComment.set(false);
    });
  }

  deleteComment(commentId: number) {
    this.api.deleteComment(this.post.id, commentId).subscribe(() => {
      this.comments.update(arr => arr.filter(c => c.id !== commentId));
    });
  }

  deletePost() {
    this.api.deletePost(this.post.id).subscribe(() => this.deleted.emit(this.post.id));
    this.menuOpen.set(false);
  }

  visibilityLabel(v: string) {
    const map: any = { PUBLIC:"Public", ALL_CONNECTIONS:"Friends", SELECTED_GROUPS:"Selected Groups", PRIVATE:"Only Me" };
    return map[v] || v;
  }

  visibilityIcon(v: string) {
    const map: any = { PUBLIC:"public", ALL_CONNECTIONS:"people", SELECTED_GROUPS:"group", PRIVATE:"lock" };
    return map[v] || "public";
  }

  visibilityClass(v: string) {
    const map: any = { PUBLIC:"badge-public", ALL_CONNECTIONS:"badge-friends", SELECTED_GROUPS:"badge-selected", PRIVATE:"badge-private" };
    return map[v] || "";
  }

  timeAgo(dateStr: string): string {
    const diff = Date.now() - new Date(dateStr).getTime();
    const m = Math.floor(diff/60000), h = Math.floor(m/60), d = Math.floor(h/24);
    if (d > 0) return `${d}d ago`;
    if (h > 0) return `${h}h ago`;
    if (m > 0) return `${m}m ago`;
    return "Just now";
  }

  get isOwner() { return this.auth.currentUser()?.id === this.post.authorId; }
  initials(name: string) { return name.split(" ").map(w=>w[0]).join("").toUpperCase().slice(0,2); }
}