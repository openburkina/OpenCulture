import { Injectable } from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AccountService} from "../auth/account.service";
import {AuthJWTService} from "../auth/auth-jwt.service";

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
    resp: boolean;
    constructor(
        private accountService: AccountService,
        private authJWT: AuthJWTService,
        private router: Router,
    ) { }
    canActivate(): boolean {
        if (this.authJWT.isAuthorities().length>0) {
            this.resp =  this.authJWT.isAuthorities().some(value => value ==='ROLE_ADMIN');
            if (this.resp) {
                return true
            }else {
                this.authJWT.logout();
                return false;
            }
        }else {
            this.authJWT.logout();
            return false;
        }

    }
}
