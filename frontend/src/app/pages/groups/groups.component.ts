import { Component, OnInit, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ApiService } from "../../core/services/api.service";
import { Group } from "../../core/models";

@Component({
  selector: "app-groups",
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: "./groups.component.html",
  styleUrl: "./groups.component.css"
})
export class GroupsComponent implements OnInit {
  groups = signal<Group[]>([]);
  loading = signal(true);
  showCreate = signal(false);
  editingGroup = signal<Group | null>(null);
  newName = signal("");
  newDesc = signal("");
  saving = signal(false);
  error = signal("");
  confirmDeleteId = signal<number | null>(null);

  GROUP_ICONS = ["family_restroom","work","flight","sports_soccer","school","restaurant","music_note","palette","fitness_center","local_movies"];

  constructor(private api: ApiService) {}

  ngOnInit() { this.load(); }

  load() {
    this.api.getGroups().subscribe({ next: g => { this.groups.set(g); this.loading.set(false); }, error: () => this.loading.set(false) });
  }

  openCreate() { this.showCreate.set(true); this.editingGroup.set(null); this.newName.set(""); this.newDesc.set(""); this.error.set(""); }
  openEdit(g: Group) { this.editingGroup.set(g); this.showCreate.set(true); this.newName.set(g.groupName); this.newDesc.set(g.description || ""); this.error.set(""); }
  closeModal() { this.showCreate.set(false); this.editingGroup.set(null); }

  save() {
    if (!this.newName().trim()) { this.error.set("Group name is required"); return; }
    this.saving.set(true); this.error.set("");
    const req = this.editingGroup()
      ? this.api.updateGroup(this.editingGroup()!.id, { groupName: this.newName(), description: this.newDesc() })
      : this.api.createGroup({ groupName: this.newName(), description: this.newDesc() });
    req.subscribe({
      next: () => { this.load(); this.closeModal(); this.saving.set(false); },
      error: (e) => { this.error.set(e.error?.message || "Failed to save group"); this.saving.set(false); }
    });
  }

  confirmDelete(id: number) { this.confirmDeleteId.set(id); }

  deleteGroup(id: number) {
    this.api.deleteGroup(id).subscribe(() => { this.groups.update(gs => gs.filter(g => g.id !== id)); this.confirmDeleteId.set(null); });
  }

  icon(index: number) { return this.GROUP_ICONS[index % this.GROUP_ICONS.length]; }

  colorClass(index: number) {
    const colors = ["violet","coral","teal","orange","blue"];
    return colors[index % colors.length];
  }
}