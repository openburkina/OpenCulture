import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LocalStorageService, SessionStorageService} from 'ngx-webstorage';
import {Router} from '@angular/router';
import {LoginVM} from '../../models/login-vm';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';
import {map} from 'rxjs/operators';
import * as jwt_decode from 'jwt-decode';

interface JwtToken {
    id_token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthJWTService {

  constructor(
      private http: HttpClient,
      private $localStorage: LocalStorageService,
      private $sessionStorage: SessionStorageService,
      private router: Router,
  ) { }
    getToken(): string {
        return (this.$sessionStorage.retrieve('authenticationToken')
            || this.$localStorage.retrieve('authenticationToken')) || '';
    }

    getTokenExpirationDate(token: string): Date {
        const decoded = jwt_decode.default(token);

        // @ts-ignore
        if (decoded.exp === undefined) return null;

        const date = new Date(0);

        // @ts-ignore
        date.setUTCSeconds(decoded.exp);
        return date;
    }

    isTokenExpired(token?: string): boolean {
        if (!token) token = this.getToken();
        if (!token) return true;

        const date = this.getTokenExpirationDate(token);
        if (date === undefined) return false;
        return !(date.valueOf() > new Date().valueOf());
    }

    login(credentials: LoginVM): Observable<void> {
        return this.http
            .post<JwtToken>(environment.SERVER_URL + '/authenticate', credentials)
            .pipe(map((response: JwtToken) => {
                    this.authenticateSuccess(response, credentials.rememberMe);
                }),
            );
    }

    logout(): void {
        this.$localStorage.clear();
        this.$sessionStorage.clear();
        this.router.navigate(['/']);
    }

    private authenticateSuccess(response: JwtToken, rememberMe: boolean): void {
        const jwt = response.id_token;
        if (rememberMe) {
            this.$localStorage.store('authenticationToken', jwt);
        } else {
            this.$sessionStorage.store('authenticationToken', jwt);
        }
    }
}
