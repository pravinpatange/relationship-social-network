import { Component, EventEmitter, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PostService, Post } from '../../core/services/post.service';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-post.component.html'
})
export class CreatePostComponent {
  @Output() postCreated = new EventEmitter<Post>();
  private postService = inject(PostService);

  caption = '';
  visibilityType = 'PUBLIC';
  isSubmitting = false;

  onSubmit() {
    if (!this.caption.trim()) return;

    this.isSubmitting = true;
    const postData = {
      caption: this.caption,
      visibilityType: this.visibilityType,
      mediaUrl: '' // In v1 we're not handling real S3 upload, just empty string or placeholder
    };

    this.postService.createPost(postData).subscribe({
      next: (post) => {
        this.postCreated.emit(post);
        this.caption = '';
        this.visibilityType = 'PUBLIC';
        this.isSubmitting = false;
      },
      error: () => {
        this.isSubmitting = false;
        alert('Failed to create post');
      }
    });
  }
}
