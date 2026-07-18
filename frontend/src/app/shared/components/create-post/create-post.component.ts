import { Component, Output, EventEmitter, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ApiService } from "../../../core/services/api.service";
import { AuthService } from "../../../core/services/auth.service";
import { Post, Group } from "../../../core/models";

@Component({
  selector: "app-create-post",
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: "./create-post.component.html",
  styleUrl: "./create-post.component.css"
})
export class CreatePostComponent {
  @Output() posted = new EventEmitter<Post>();

  expanded = signal(false);
  caption = signal("");
  mediaUrl = signal("");
  visibility = signal<"PUBLIC"|"ALL_CONNECTIONS"|"SELECTED_GROUPS"|"PRIVATE">("ALL_CONNECTIONS");
  selectedGroupIds = signal<number[]>([]);
  groups = signal<Group[]>([]);
  loading = signal(false);
  showGroups = signal(false);

  visibilityOptions = [
    { value: "PUBLIC",          icon: "public",   label: "Public",          desc: "Everyone can see" },
    { value: "ALL_CONNECTIONS", icon: "people",   label: "All Friends",     desc: "Your accepted friends" },
    { value: "SELECTED_GROUPS", icon: "group",    label: "Selected Groups", desc: "Choose specific groups" },
    { value: "PRIVATE",         icon: "lock",     label: "Only Me",         desc: "Private — just you" },
  ];

  constructor(private api: ApiService, public auth: AuthService) {}

  expand() {
    this.expanded.set(true);
    if (this.groups().length === 0) this.api.getGroups().subscribe(g => this.groups.set(g));
  }

  toggleGroup(id: number) {
    this.selectedGroupIds.update(ids => ids.includes(id) ? ids.filter(i=>i!==id) : [...ids, id]);
  }

  submit() {
    const caption = this.caption().trim();
    const mediaUrl = this.mediaUrl().trim();
    if (!caption && !mediaUrl) return;
    if (this.visibility() === "SELECTED_GROUPS" && this.selectedGroupIds().length === 0) return;

    this.loading.set(true);
    this.api.createPost({
      caption: caption || undefined,
      mediaUrl: mediaUrl || undefined,
      visibilityType: this.visibility(),
      visibleToGroupIds: this.visibility() === "SELECTED_GROUPS" ? this.selectedGroupIds() : undefined
    }).subscribe({
      next: (p) => { this.posted.emit(p); this.reset(); },
      error: () => this.loading.set(false)
    });
  }

  reset() {
    this.caption.set(""); this.mediaUrl.set("");
    this.visibility.set("ALL_CONNECTIONS"); this.selectedGroupIds.set([]);
    this.expanded.set(false); this.loading.set(false);
  }

  get currentUser() { return this.auth.currentUser(); }
  initials(name?: string) { if(!name) return "?"; return name.split(" ").map(w=>w[0]).join("").toUpperCase().slice(0,2); }
  get selectedVisibility() { return this.visibilityOptions.find(v=>v.value===this.visibility()); }
}