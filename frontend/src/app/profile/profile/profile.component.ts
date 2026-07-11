import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../core/services/user.service';
import { User } from '../../core/services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  private userService = inject(UserService);

  user: User | null = null;
  isEditing = false;
  isLoading = true;
  editData: any = {};

  ngOnInit() {
    this.loadProfile();
  }

  loadProfile() {
    this.isLoading = true;
    this.userService.getCurrentUser().subscribe({
      next: (data) => {
        this.user = data;
        this.editData = { ...data };
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
    if (!this.isEditing) {
      this.editData = { ...this.user }; // reset changes
    }
  }

  saveProfile() {
    this.userService.updateProfile(this.editData).subscribe({
      next: (updated) => {
        this.user = updated;
        this.isEditing = false;
      },
      error: () => alert('Failed to update profile')
    });
  }
}
