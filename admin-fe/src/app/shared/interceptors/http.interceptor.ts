import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from 'src/environment/environment';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private toastr: ToastrService) { }

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
             this.toastr.error('Session timeout. Please login again.');
             setTimeout(() => {
               window.location.href = '/';
             }, 1500)
           } else if (error.status === 400 && error.message === 'Access is denied') {
             this.toastr.error('Session timeout. Please login again.');
             setTimeout(() => {
               window.location.href = '/';
             }, 1500)
           }
          return throwError(() => error);
        })
      );
    }
    return next.handle(request);
  }
}
