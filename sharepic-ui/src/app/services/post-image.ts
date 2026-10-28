import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class PostImageService{
    constructor(private client: HttpClient){}
    getPostImage(postId: string){
        return this.client.get(
            `http://localhost:8080/api/v1/posts/${postId}/image`,
            {responseType: 'blob'});
    }
}