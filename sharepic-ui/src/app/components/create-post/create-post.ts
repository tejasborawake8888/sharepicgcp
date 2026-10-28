import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Post, PostResponse } from '../../services/post';

@Component({
  selector: 'app-create-post',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-post.html',
  styleUrl: './create-post.css'
})
export class CreatePost {
  @Output() postCreated = new EventEmitter<PostResponse>();
  @Output() cancelled = new EventEmitter<void>();

  caption = '';
  selectedImage: File | null = null;
  imagePreview: string | null = null;
  submitting = false;
  errorMessage = '';

  constructor(private postService: Post) {}

  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;

    this.selectedImage = file;
    this.imagePreview = null;

    if (file) {
      this.imagePreview = URL.createObjectURL(file);
    }
  }

  submitPost(): void {
    if (!this.caption.trim()) {
      this.errorMessage = 'Please enter a caption.';
      return;
    }

    this.submitting = true;
    this.errorMessage = '';

    this.postService.createPost(this.caption.trim(), this.selectedImage)
      .subscribe({
        next: (post) => {
          this.submitting = false;
          this.postCreated.emit(post);
        },
        error: (err) => {
          console.error('Failed to create post', err);
          this.submitting = false;
          this.errorMessage = 'Could not create post. Please try again.';
        }
      });
  }
}