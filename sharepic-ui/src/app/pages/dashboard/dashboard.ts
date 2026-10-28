import { Component,OnInit,OnDestroy } from '@angular/core';
import { Subscription,timer,switchMap } from 'rxjs';
import { Feed } from '../../services/feed';
import { PostResponse } from '../../services/post';
import { DatePipe } from '@angular/common';

@Component({
  standalone: true,
  imports: [DatePipe],
  selector: 'app-dashboard',
  styleUrl: './dashboard.css',
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit,OnDestroy{
  constructor(private feed: Feed){}
  posts: PostResponse[] = []
  loading=true;
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
        
        console.log('Feed API response:', posts);
        console.log('Is array:', Array.isArray(posts));

        this.posts = posts;
        this.loading = false;
        console.log('Posts assigned:', this.posts.length);
      },
      error: (err) => {
        console.error('Failed to load feed',err)
        this.loading = false
      }
    })
  }
  ngOnDestroy(): void {
    this.feedSubscription?.unsubscribe();
  }

}
