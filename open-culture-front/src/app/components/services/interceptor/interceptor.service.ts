import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {LocalStorageService, SessionStorageService} from 'ngx-webstorage';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService {

  constructor(
      private $localStorage: LocalStorageService, private $sessionStorage: SessionStorageService
  ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        console.log('TEST');
        console.log(request);
        if (!request || !request.url || (request.url.startsWith('http') &&
            !(environment.SERVER_URL && request.url.startsWith(environment.SERVER_URL)))) {

            return next.handle(request);
        }

        const token = this.$localStorage.retrieve('authenticationToken') || this.$sessionStorage.retrieve('authenticationToken');
        if (token) {
            console.log(token);
            request = request.clone({
                setHeaders: {
                    Authorization: 'Bearer ' + token
                }
            });
        }
        console.log(request);
        return next.handle(request);
    }
}
