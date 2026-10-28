import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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
  private readonly apiUrl = 'http://localhost:8082/api/v1/posts';

  constructor(private http: HttpClient) {}

  likePost(postId: string): Observable<PostLikeResponse> {
    return this.http.post<PostLikeResponse>(
      `${this.apiUrl}/${postId}/like`,
      {}
    );
  }
}