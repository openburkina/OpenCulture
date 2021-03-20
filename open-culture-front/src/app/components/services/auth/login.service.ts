import { Injectable } from '@angular/core';

import {AccountService} from './account.service';
import {AuthJWTService} from './auth-jwt.service';
import {LoginVM} from '../../models/login-vm';
import {Observable} from 'rxjs';
import {flatMap} from 'rxjs/operators';
import {Account} from '../../models/account';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(
      private accountService: AccountService,
      private authServerProvider: AuthJWTService,
  ) { }

    login(credentials: LoginVM): Observable<Account | null> {
        return this.authServerProvider.login(credentials).pipe(flatMap(() => this.accountService.identity(true)));
    }

    logout(): void {
        this.authServerProvider.logout();
    }
}
