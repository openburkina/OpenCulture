import {Injectable, isDevMode} from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {SessionStorageService} from 'ngx-webstorage';
import {AccountService} from './account.service';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {
 
  constructor(
    private httpClient: HttpClient,
    private router: Router,
    private accountService: AccountService,
    private $sessionStorage: SessionStorageService,
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    const authorities = route.data['authorities'];
    // We need to call the checkLogin / and so the accountService.identity() function, to ensure,
    // that the client has a principal too, if they already logged in by the server.
    // This could happen on a page refresh.

    return this.checkLogin(authorities, state.url);
  }

  storeUrl(url: string): void {
    this.$sessionStorage.store('previousUrl', url);
  }

  getUrl(): any {
    return this.$sessionStorage.retrieve('previousUrl');
  }

  checkLogin(authorities: string[], url: string): Observable<boolean> {
    return this.accountService.identity().pipe(
      map(account => {
        //  const auth = this.httpClient.get(environment.apiUrl + 'api/authenticate');
        //  console.log(auth);
        //  if( account != null){
        //     return true;
        //  } else {
        //   this.storeUrl(url);
        //   this.router.navigate(['']);
        //   this.router.navigateByUrl('sign-in');
        //   return false;
        //  }

        // if (!authorities || authorities.length === 0) {
        //   console.log(authorities);
        //   return true;
        // }
        console.log(account);
        if (account) {
          const hasAnyAuthority = this.accountService.hasAnyAuthority(authorities);
          if (hasAnyAuthority) {
            console.log(hasAnyAuthority);
            return true;
          }
          if (isDevMode()) {
            console.error('User has not any of required authorities: ', authorities);
          }
          // console.log("error");
          // this.router.navigate(['error']);
          // return false;
        }

        this.storeUrl(url);
        this.router.navigate(['']);
        this.router.navigateByUrl('sign-in');
        return false;
      })
    );
  }
}
