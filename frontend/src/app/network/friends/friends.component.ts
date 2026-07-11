import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FriendService, Friendship } from '../../core/services/friend.service';
import { GroupService, Group } from '../../core/services/group.service';

@Component({
  selector: 'app-friends',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './friends.component.html'
})
export class FriendsComponent implements OnInit {
  private friendService = inject(FriendService);
  private groupService = inject(GroupService);

  friendships: Friendship[] = [];
  groups: Group[] = [];
  newFriendId: number | null = null;
  isLoading = true;

  ngOnInit() {
    this.loadFriends();
    this.groupService.getGroups().subscribe(groups => this.groups = groups);
  }

  loadFriends() {
    this.isLoading = true;
    this.friendService.getFriends().subscribe({
      next: (data) => {
        this.friendships = data;
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  get pendingRequests() {
    return this.friendships.filter(f => f.status === 'PENDING');
  }

  get acceptedFriends() {
    return this.friendships.filter(f => f.status === 'ACCEPTED');
  }

  sendRequest() {
    if (!this.newFriendId) return;
    this.friendService.sendRequest(this.newFriendId).subscribe(() => {
      this.newFriendId = null;
      this.loadFriends(); // reload to show pending
      alert('Request sent!');
    });
  }

  acceptRequest(id: number) {
    this.friendService.acceptRequest(id).subscribe(() => {
      this.loadFriends();
    });
  }

  rejectRequest(id: number) {
    this.friendService.rejectRequest(id).subscribe(() => {
      this.loadFriends();
    });
  }

  // Assign to group modal logic could go here in v2
}
