import { Component, inject, OnInit, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService, Post } from '../../core/services/post.service';
import { ModeService } from '../../core/services/mode.service';
import { PostItemComponent } from '../post-item/post-item.component';
import { CreatePostComponent } from '../create-post/create-post.component';

@Component({
  selector: 'app-feed',
  standalone: true,
  imports: [CommonModule, PostItemComponent, CreatePostComponent],
  templateUrl: './feed.component.html'
})
export class FeedComponent implements OnInit {
  private postService = inject(PostService);
  public modeService = inject(ModeService);

  posts: Post[] = [];
  isLoading = true;

  constructor() {
    effect(() => {
      // Reload feed when mode changes
      const currentMode = this.modeService.currentMode();
      this.loadFeed(currentMode.activeMode, currentMode.activeGroupId);
    });
  }

  ngOnInit() {}

  loadFeed(modeName: string, groupId?: number) {
    this.isLoading = true;
    this.postService.getFeed(modeName, groupId).subscribe({
      next: (data) => {
        this.posts = data;
        this.isLoading = false;
      },
      error: () => {
        this.posts = [];
        this.isLoading = false;
      }
    });
  }

  onPostCreated(newPost: Post) {
    this.posts.unshift(newPost);
  }

  onPostDeleted(postId: number) {
    this.posts = this.posts.filter(p => p.id !== postId);
  }
}
