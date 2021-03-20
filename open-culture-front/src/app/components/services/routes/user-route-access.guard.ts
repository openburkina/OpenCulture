import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import { Observable } from 'rxjs';
import {AccountService} from "../auth/account.service";
import {AuthJWTService} from "../auth/auth-jwt.service";

@Injectable({
  providedIn: 'root'
})
export class UserRouteAccessGuard implements CanActivate {

    constructor(
        private accountService: AccountService,
        private authJWT: AuthJWTService,
        private router: Router,
    ) { }
    canActivate(): boolean {
        console.log('UserRouteAccessService');
        if (!this.authJWT.isTokenExpired()) {
            return true;
        }
        this.router.navigate(['/login']);
        return false;
    }
}
