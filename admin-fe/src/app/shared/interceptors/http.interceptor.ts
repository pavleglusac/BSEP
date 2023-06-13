import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from 'src/environment/environment';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    request = request.clone({ url: `${environment.baseUrl}/${request.url}`, withCredentials: true });
    const token = sessionStorage.getItem(environment.tokenName);
    if (token) {
      const authReq = request.clone({
        headers: new HttpHeaders({
          Authorization: `Bearer ${token}`,
          AccessControlAllowOrigin: '*',
        }),

      });
      return next.handle(authReq).pipe(
        catchError((error) => {
          if (error.status === 401 && window.location.pathname !== '/') {
            window.location.href = '/';
          } else if (error.status === 400 && error.message === 'Access is denied') {
            window.location.href = '/';
          }
          return throwError(() => error);
        })
      );
    }
    return next.handle(request);
  }
}
