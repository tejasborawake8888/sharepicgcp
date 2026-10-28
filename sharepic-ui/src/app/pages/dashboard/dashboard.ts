import { Component,OnInit,OnDestroy,ChangeDetectorRef } from '@angular/core';
import { Subscription,timer,switchMap } from 'rxjs';
import { Feed } from '../../services/feed';
import { PostResponse } from '../../services/post';
import { DatePipe,NgIf } from '@angular/common';
import { PostImageService } from '../../services/post-image';
import { CreatePost } from '../../components/create-post/create-post';
import { PostLike } from '../../services/post-like';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { JwtTokenStorage } from '../../services/jwt-token-storage';

@Component({
  standalone: true,
  imports: [DatePipe,NgIf,CreatePost],
  selector: 'app-dashboard',
  styleUrl: './dashboard.css',
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit,OnDestroy{
  constructor(private feed: Feed,
    private cdr:ChangeDetectorRef,
    private postImage: PostImageService,
    private postLike: PostLike,
    private jwtTokenStorage: JwtTokenStorage,
    private router: Router
  ){}
  posts: PostResponse[] = []
  loading=true;
  showCreatePost = false;
  likedPostIds = new Set<string>();
  likeMessages: { [postId: string]: string } = {};
  imageUrls: { [postId: string]: string } = {};
  feedSubscription?: Subscription

  ngOnInit(): void {
    console.log('DashBoard initialized')
    this.feedSubscription = timer(0,60000)
    .pipe(
      switchMap(() => {
        console.log('Requesting Feed .....');
        return this.feed.getFeed()})
    )
    .subscribe({
      next: (posts) => {        
        this.posts = posts;
        posts.forEach(post => {
          if (post.imageUrl && !this.imageUrls[post.id]) {
            this.loadPostImage(post.id);
          }
        })
        this.loading = false;
        this.cdr.detectChanges();
      
      },
      error: (err) => {
        console.error('Failed to load feed',err)
        this.loading = false
        this.cdr.detectChanges();
      }
    })
  }
  ngOnDestroy(): void {
    this.feedSubscription?.unsubscribe();
  }

  loadPostImage(postId: string): void {
    this.postImage.getPostImage(postId).subscribe({
      next: (blob) => {
        this.imageUrls[postId] = URL.createObjectURL(blob);
        this.cdr.detectChanges();
      },
      error: (err) => {
        if (err.status === 404) {
          console.log(`No image for post ${postId}`);
          return;
        }

        console.error(`Failed to load image for post ${postId}`, err);
      }
    });
  }

  openCreatePost(): void {
  this.showCreatePost = true;
  }

  closeCreatePost(): void {
    this.showCreatePost = false;
  }

  onPostCreated(): void {
    this.showCreatePost = false;
    this.feedSubscription?.unsubscribe();

    this.feedSubscription = timer(0)
      .pipe(switchMap(() => this.feed.getFeed()))
      .subscribe({
        next: (posts) => {
          this.posts = posts;

          posts.forEach(post => {
            if (post.imageUrl && !this.imageUrls[post.id]) {
              this.loadPostImage(post.id);
            }
          });

          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Failed to refresh feed', err);
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
  }
  likePost(postId: string): void {
    if (this.likedPostIds.has(postId)) {
      return;
    }

    this.likeMessages[postId] = '';

    this.postLike.likePost(postId).subscribe({
      next: () => {
        this.likedPostIds.add(postId);
        this.cdr.detectChanges();
      },
      error: (err: HttpErrorResponse) => {
        if (err.status === 400) {
          this.likeMessages[postId] = 'You cannot like your own post.';
        } else if (err.status === 409) {
          this.likeMessages[postId] = 'You have already liked this post.';
        } else {
          this.likeMessages[postId] = 'Could not like this post. Please try again.';
        }

        this.cdr.detectChanges();
      }
    });
  }
  logout(): void {
    this.jwtTokenStorage.clearToken();
    this.router.navigate(['/login']);
  }
}
