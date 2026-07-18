import { Component, OnInit, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { ApiService } from "../../core/services/api.service";
import { AuthService } from "../../core/services/auth.service";
import { User, Post } from "../../core/models";
import { PostCardComponent } from "../../shared/components/post-card/post-card.component";

type ProfileTab = "posts" | "about";

@Component({
  selector: "app-profile",
  standalone: true,
  imports: [CommonModule, FormsModule, PostCardComponent],
  templateUrl: "./profile.component.html",
  styleUrl: "./profile.component.css"
})
export class ProfileComponent implements OnInit {
  user = signal<User | null>(null);
  posts = signal<Post[]>([]);
  loading = signal(true);
  loadingPosts = signal(true);
  isOwnProfile = signal(false);
  tab = signal<ProfileTab>("posts");
  editing = signal(false);
  saving = signal(false);

  editForm = signal({ displayName: "", bio: "", location: "", website: "", accountType: "PRIVATE" as "PUBLIC"|"PRIVATE" });

  constructor(private route: ActivatedRoute, private api: ApiService, public auth: AuthService) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      const id = params["id"];
      if (!id) {
        this.isOwnProfile.set(true);
        this.api.getMe().subscribe(u => { this.user.set(u); this.loading.set(false); this.loadMyPosts(); });
      } else {
        this.isOwnProfile.set(+id === this.auth.currentUser()?.id);
        this.api.getUserById(+id).subscribe({ next: u => { this.user.set(u); this.loading.set(false); this.loadMyPosts(); }, error: () => this.loading.set(false) });
      }
    });
  }

  loadMyPosts() {
    if (!this.isOwnProfile()) { this.loadingPosts.set(false); return; }
    this.api.getMyPosts().subscribe({ next: p => { this.posts.set(p); this.loadingPosts.set(false); }, error: () => this.loadingPosts.set(false) });
  }

  openEdit() {
    const u = this.user()!;
    this.editForm.set({ displayName: u.displayName, bio: u.bio||"", location: u.location||"", website: u.website||"", accountType: u.accountType });
    this.editing.set(true);
  }

  saveProfile() {
    this.saving.set(true);
    this.api.updateProfile(this.editForm()).subscribe({ next: u => { this.user.set(u); this.auth.loadCurrentUser(); this.editing.set(false); this.saving.set(false); }, error: () => this.saving.set(false) });
  }

  onPostDeleted(id: number) { this.posts.update(ps => ps.filter(p => p.id !== id)); }

  initials(name?: string) { if(!name) return "?"; return name.split(" ").map(w=>w[0]).join("").toUpperCase().slice(0,2); }

  memberSince(d?: string) { if(!d) return ""; return new Date(d).toLocaleDateString("en-US",{month:"long",year:"numeric"}); }
}