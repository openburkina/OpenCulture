import { Injectable } from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {AuthJWTService} from "../auth/auth-jwt.service";

@Injectable({
  providedIn: 'root'
})
export class UserRouteAccesGuard implements CanActivate {
    constructor(
        private authJWT: AuthJWTService,
        private router: Router,
    ) { }
    canActivate(): boolean {
        if (!this.authJWT.isTokenExpired()) {
            return true;
        }
        this.router.navigate(['/']);
        return false;
    }
}
