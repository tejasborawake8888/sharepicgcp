import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_CONFIG } from './api-config';

export enum Role{
    USER = 'USER',
    ADMIN = 'ADMIN'
}
export interface SignupRequest{
    userName: string;
    email: string;
    password: string;
}
export interface SignupResponse{
    id: string;
    userName: string;
    email: string;
    role: Role;
    createdAt: string;
}

export interface LoginRequest{
    userNameOrEmail: string,
    password: string 
}

export interface LoginResponse{
    id: string,
    userName: string,
    email: string,
    role: Role,
    accessToken: string
}

@Injectable({providedIn: 'root'})
export class Auth {
    constructor(private client:HttpClient ){}
    registerUser(signupRequest: SignupRequest):Observable<SignupResponse>{
        return this.client.post<SignupResponse>(`${API_CONFIG.user}/users`,signupRequest);
    }
    login(loginRequest: LoginRequest):Observable<LoginResponse>{
        return this.client.post<LoginResponse>(`${API_CONFIG.user}/auth/login`,loginRequest);
    }
}
