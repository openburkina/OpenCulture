import { AuthJwtService } from './../service/auth-jwt.service';
import { Injectable } from '@angular/core';
import {flatMap, mergeMap} from 'rxjs/operators';
import {AccountService} from '../service/account.service';

@Injectable({ providedIn: 'root' })
export class SigninService {
    constructor(private authServerProvider: AuthJwtService, private accountService: AccountService) {}

    public login(credentials) {
        return this.authServerProvider.login(credentials).pipe(flatMap(() => this.accountService.identity(true)));
    }

    logout() {
        this.authServerProvider.logout().subscribe(null,null,() => (this.accountService.authenticate(null)));
    }
}
