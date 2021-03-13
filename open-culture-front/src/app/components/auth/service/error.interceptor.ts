import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {AuthenticateService} from './authenticate.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private authenticateService: AuthenticateService){}
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(catchError(err => {
      if (err.status === 401) {
        this.authenticateService.logout();
        location.reload();
      }

      const error = err.error.message || err.statusText;
      return throwError(error);
    }));
  }

}
