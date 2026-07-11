import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Post, PostService } from '../../core/services/post.service';
import { InteractionService, Comment } from '../../core/services/interaction.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-post-item',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './post-item.component.html'
})
export class PostItemComponent {
  @Input() post!: Post;
  @Output() postDeleted = new EventEmitter<number>();
  
  private postService = inject(PostService);
  private interactionService = inject(InteractionService);
  private authService = inject(AuthService);

  comments: Comment[] = [];
  showComments = false;
  newCommentText = '';
  isLiking = false;

  get isOwner() {
    return this.post.userId === this.authService.currentUser()?.id;
  }

  toggleLike() {
    if (this.isLiking) return;
    this.isLiking = true;
    
    if (this.post.hasLiked) {
      this.interactionService.unlikePost(this.post.id).subscribe({
        next: () => {
          this.post.hasLiked = false;
          this.post.likeCount = (this.post.likeCount || 1) - 1;
          this.isLiking = false;
        },
        error: () => this.isLiking = false
      });
    } else {
      this.interactionService.likePost(this.post.id).subscribe({
        next: () => {
          this.post.hasLiked = true;
          this.post.likeCount = (this.post.likeCount || 0) + 1;
          this.isLiking = false;
        },
        error: () => this.isLiking = false
      });
    }
  }

  toggleComments() {
    this.showComments = !this.showComments;
    if (this.showComments && this.comments.length === 0) {
      this.loadComments();
    }
  }

  loadComments() {
    this.interactionService.getComments(this.post.id).subscribe(data => {
      this.comments = data;
    });
  }

  addComment() {
    if (!this.newCommentText.trim()) return;
    this.interactionService.addComment(this.post.id, this.newCommentText).subscribe(comment => {
      this.comments.push(comment);
      this.post.commentCount = (this.post.commentCount || 0) + 1;
      this.newCommentText = '';
    });
  }

  deletePost() {
    if (confirm('Are you sure you want to delete this post?')) {
      this.postService.deletePost(this.post.id).subscribe(() => {
        this.postDeleted.emit(this.post.id);
      });
    }
  }
}
