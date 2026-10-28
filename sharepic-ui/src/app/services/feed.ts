import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable } from "rxjs";
import { PostResponse } from "./post";

@Injectable({providedIn: 'root'})
export class Feed{
    constructor(private client: HttpClient){}
    getFeed():Observable<PostResponse[]>{
        return this.client.get<PostResponse[]>("http://localhost:8082/api/v1/feed");
    }
}