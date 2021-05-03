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
        return this.resp =  this.authJWT.isAuthorities().some(value => value ==='ROLE_ADMIN');
    }
}