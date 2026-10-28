
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { JwtTokenStorage } from './jwt-token-storage';

export const authInterceptor: HttpInterceptorFn = (req,next) => {
    const jwtTokenStorage = inject(JwtTokenStorage)
    const token = jwtTokenStorage.getToken()
    console.log('Interceptor running:', !!token);
    if(token){
        const clonedReq = req.clone({
            setHeaders:{
                "Authorization": `Bearer ${token}`
            }
        })
        console.log('Authorization header added:', 
        clonedReq.headers.has('Authorization'));
        return next(clonedReq)
    }

    return next(req)
}