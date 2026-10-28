import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { API_CONFIG } from './api-config';

export interface PostResponse{
    id: string,
    userId: string,
    caption: string,
    imageUrl: string,
    createdAt: string,
    updatedAt: string
}

export interface PostResponse {
  id: string;
  userId: string;
  caption: string;
  imageUrl: string;
  createdAt: string;
  updatedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class Post {
  private readonly apiUrl = `${API_CONFIG.post}/posts`;

  constructor(private http: HttpClient) {} 

  createPost(caption: string, image: File | null): Observable<PostResponse> {
    const formData = new FormData();
    formData.append('caption', caption);

    if (image) {
      formData.append('image', image);
    }

    return this.http.post<PostResponse>(this.apiUrl, formData);
  }
}