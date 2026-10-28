import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable } from "rxjs";
import { PostResponse } from "./post";
import { API_CONFIG } from './api-config';

@Injectable({providedIn: 'root'})
export class Feed{
    constructor(private client: HttpClient){}
    getFeed():Observable<PostResponse[]>{
        return this.client.get<PostResponse[]>(
            `${API_CONFIG.feed}/feed`
        );
    }
}