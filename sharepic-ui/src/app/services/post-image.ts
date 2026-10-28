import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { API_CONFIG } from './api-config';

@Injectable({ providedIn: 'root' })
export class PostImageService{
    constructor(private client: HttpClient){}
    getPostImage(postId: string){
        return this.client.get(
            `${API_CONFIG.post}/posts/${postId}/image`,
            { responseType: 'blob' }
        );
    }
}