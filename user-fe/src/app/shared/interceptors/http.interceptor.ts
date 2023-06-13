import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { baseUrl, tokenName } from '../constants';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    request = request.clone({ url: `${baseUrl}/${request.url}`, withCredentials: true });
    const token = sessionStorage.getItem(tokenName);
    if (token) {
      const authReq = request.clone({
        headers: new HttpHeaders({
          Authorization: `Bearer ${token}`,
        }),
      });
      return next.handle(authReq).pipe(
        catchError((error) => {
          console.log(error.message)
          if (error.status === 401 && window.location.pathname !== '/login') {
            window.location.href = '/login';
          } else if (error.status === 400 && error.message === 'Access is denied') {
            window.location.href = '/login';
          }
          return throwError(() => error);
        })
      );
    }
    return next.handle(request);
  }
}
