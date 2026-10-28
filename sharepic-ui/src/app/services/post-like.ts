import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from './api-config';

export interface PostLikeResponse {
  id: {
    postId: string;
    userId: string;
  };
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class PostLike {
  private readonly apiUrl = `${API_CONFIG.feed}/posts`;

  constructor(private http: HttpClient) {}

  likePost(postId: string): Observable<PostLikeResponse> {
    return this.http.post<PostLikeResponse>(
      `${this.apiUrl}/${postId}/like`,
      {}
    );
  }
}