import { Injectable } from '@angular/core';

import {AccountService} from './account.service';
import {AuthJWTService} from './auth-jwt.service';
import {LoginVM} from '../../models/login-vm';
import {Observable} from 'rxjs';
import {flatMap} from 'rxjs/operators';
import {Account} from '../../models/account';
import {User} from "../../models/User";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private user : User;
  constructor(
      private accountService: AccountService,
      private authServerProvider: AuthJWTService,
  ) {
      this.user = new User();
  }

    login(credentials: LoginVM): Observable<Account | null> {
        return this.authServerProvider.login(credentials).pipe(flatMap(() => this.accountService.identity(true)));
    }

    logout(): void {
        this.authServerProvider.logout();
    }


    getUser(): User {
        return this.user;
    }

    setUser(value: User) {
        this.user = value;
    }
}
