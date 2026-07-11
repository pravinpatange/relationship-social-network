import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GroupService, Group } from '../../core/services/group.service';

@Component({
  selector: 'app-groups',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './groups.component.html'
})
export class GroupsComponent implements OnInit {
  private groupService = inject(GroupService);

  groups: Group[] = [];
  newGroupName = '';
  newGroupDescription = '';
  isLoading = true;

  ngOnInit() {
    this.loadGroups();
  }

  loadGroups() {
    this.isLoading = true;
    this.groupService.getGroups().subscribe({
      next: (data) => {
        this.groups = data;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  createGroup() {
    if (!this.newGroupName.trim()) return;

    const data = {
      groupName: this.newGroupName,
      description: this.newGroupDescription
    };

    this.groupService.createGroup(data).subscribe(() => {
      this.newGroupName = '';
      this.newGroupDescription = '';
      this.loadGroups();
    });
  }

  deleteGroup(id: number) {
    if (confirm('Are you sure you want to delete this group?')) {
      this.groupService.deleteGroup(id).subscribe(() => {
        this.groups = this.groups.filter(g => g.id !== id);
      });
    }
  }
}
