import { Injectable } from "@angular/core";

@Injectable({providedIn: 'root'})
export class JwtTokenStorage{
    
    saveToken(token: string): void{
        sessionStorage.setItem("access_token",token)
    }

    getToken(): string | null{
        return sessionStorage.getItem("access_token");
    }

    clearToken():void{
        sessionStorage.removeItem("access_token")
    }
}