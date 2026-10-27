
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { JwtTokenStorage } from './JwtTokenStorage';

export const authInterceptor: HttpInterceptorFn = (req,next) => {
    const jwtTokenStorage = inject(JwtTokenStorage)
    const token = jwtTokenStorage.getToken()
    if(token !== null){
        const clonedReq = req.clone({
            setHeaders:{
                "Authorization": `Bearer ${token}`
            }
        })
        return next(clonedReq)
    }

    return next(req)
}