import { Injectable } from '@angular/core';
import {Observable, of, ReplaySubject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {SessionStorageService} from 'ngx-webstorage';
import {Account} from '../../models/account';
import {environment} from '../../../../environments/environment';
import {catchError, shareReplay, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

    private userIdentity: Account | null = null;
    private authenticationState = new ReplaySubject<Account | null>(1);
    private accountCache$?: Observable<Account | null>;
  constructor(
      private http: HttpClient,
      private router: Router,
      private $sessionStorage: SessionStorageService,
  ) { }

    save(account: Account): Observable<{}> {
        return this.http.post(environment.apiUrl + 'account', account);
    }

    authenticate(identity: Account | null): void {
        this.userIdentity = identity;
        this.authenticationState.next(this.userIdentity);
    }

    hasAnyAuthority(authorities: string[] | string): boolean {
        if (!this.userIdentity || !this.userIdentity.authorities) {
            return false;
        }
        if (!Array.isArray(authorities)) {
            authorities = [authorities];
        }
        return this.userIdentity.authorities.some((authority: string) => authorities.includes(authority));
    }

    identity(force?: boolean): Observable<Account | null> {
        if (!this.accountCache$ || force || !this.isAuthenticated()) {
            this.accountCache$ = this.fetch().pipe(
                catchError(() => {
                    return of(null);
                }),
                tap((account: Account | null) => {
                    console.log(account);
                    this.authenticate(account);
                    console.log(account);
                    if (account) {
                     //   this.parameter.setLoggerUser(account);
                        this.navigateToStoredUrl();
                    }
                }),
                shareReplay(),
            );
        }
        return this.accountCache$;
    }

    isAuthenticated(): boolean {
        return this.userIdentity !== null;
    }

    getAuthenticationState(): Observable<Account | null> {
        return this.authenticationState.asObservable();
    }

    getImageUrl(): string {
        return this.userIdentity ? this.userIdentity.imageUrl : '';
    }

    private fetch(): Observable<Account> {
        return this.http.get<Account>(environment.apiUrl + 'account');
    }

    private navigateToStoredUrl(): void {
        // previousState can be set in the authExpiredInterceptor and in the userRouteAccessService
        // if login is successful, go to stored previousState and clear previousState
        const previousUrl = this.$sessionStorage.retrieve('url');
        if (previousUrl) {
            this.$sessionStorage.clear('url');
            this.router.navigateByUrl(previousUrl);
        }
    }
}
