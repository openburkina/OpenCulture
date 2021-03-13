import { Injectable } from '@angular/core';
import {Observable, of, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../environments/environment';
import {catchError, shareReplay, tap} from 'rxjs/operators';
import {Account} from '../model/account.model';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  userIdentity: Account;
  authenticated: boolean;
  authenticationState = new Subject<any>();
  accountCache$: Observable<Account>;

  constructor(private httpClient: HttpClient) {
    this.authenticated = false;
  }

  fetch(): Observable<any> {
      console.info('cool' );
      return this.httpClient.get<Account>(environment.apiUrl + 'api/account');
  }

  save(account: Account): Observable<Account>{
    return this.httpClient.post<Account>(environment.apiUrl + 'api/account', account);
  }

  authenticate(identity){
    this.userIdentity = identity;
    this.authenticated = identity !== null;
    this.authenticationState.next(this.userIdentity);
  }

  hasAnyAuthority(authorities: string[] | string): boolean {
    if (!this.userIdentity || !this.authenticated || !this.userIdentity.authorities){
      return false;
    }

    if (!Array.isArray(authorities)){
      authorities = [authorities];
    }
    return authorities.some((authority: string) => this.userIdentity.authorities.includes(authority));
  }

  identity(force?: boolean): Observable<Account>{
    if (force || !this.authenticated){
      this.accountCache$ = null;
    }
    if (!this.accountCache$){
      this.accountCache$ = this.fetch().pipe(
        catchError(() => {
            console.info('error');
            return of(null);
          }),
        tap(account => {
            if (account){
              console.info('cool');
              this.userIdentity = account;
              this.authenticated = true;
            } else {
              console.info('book');
              this.userIdentity = null;
              this.authenticated = false;
            }
            console.info('bbbbbbk');
            this.authenticationState.next(this.userIdentity);
          }),
        shareReplay()
      );
    }
    return  this.accountCache$;
  }

  isAuthenticated(): boolean{
    return this.authenticated;
  }

  isIdentityResolved(): boolean{
    return this.userIdentity !== undefined;
  }

  getAuthenticationState(): Observable<any>{
    return this.authenticationState.asObservable();
  }

  getImageUrl(): string{
    return this.isIdentityResolved() ? this.userIdentity.imageUrl : null;
  }
}
