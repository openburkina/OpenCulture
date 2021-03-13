import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LocalStorageService, SessionStorageService} from 'ngx-webstorage';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthJwtService {

  constructor(
    private httpClient: HttpClient,
    private $localStorage: LocalStorageService,
    private $sessionStorage: SessionStorageService) { }

  login(credentials): Observable<HttpClient>{
    const donnees = {
      username: credentials.username,
      password: credentials.password,
      rememberMe: credentials.rememberMe
    };

    function authenticationSuccess(resp): string {
      const bearerToken = resp.headers.get('Authorization');
      if (bearerToken && bearerToken.slice(0, 7) === 'Bearer '){
        const jwt = bearerToken.slice(7, bearerToken.length);
        this.storeAuthenticationToken(jwt, credentials.rememberMe);
        return jwt;
      }
    }
    return this.httpClient.post(environment.apiUrl + 'api/authenticate', donnees, { observe: 'response'})
        .pipe(map(authenticationSuccess.bind(this)));
  }
  storeAuthenticationToken(jwt, rememberMe): void{
    if (rememberMe){
        this.$localStorage.store('authenticationToken', jwt);
    }
    else {
      this.$sessionStorage.store('authenticationToken', jwt);
    }
  }

  logout(): Observable<any>{
    return new Observable(observer => {
      this.$localStorage.clear('authenticationToken');
      this.$sessionStorage.clear('authenticationToken');
      observer.complete();
    });
  }

  getToken(): any {
    return this.$localStorage.retrieve('authenticationToken') || this.$sessionStorage.retrieve('authenticationToken');
  }
}
