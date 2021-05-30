import { Injectable } from '@angular/core';
import {CanActivate,Router} from '@angular/router';
import {AccountService} from "../auth/account.service";
import {AuthJWTService} from "../auth/auth-jwt.service";
import {SignInComponent} from '../../pages/sign-in/sign-in.component';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Injectable({
  providedIn: 'root'
})
export class UserRouteAccessGuard implements CanActivate {

    constructor(
        private accountService: AccountService,
        private authJWT: AuthJWTService,
        private router: Router,
        private modal: NgbModal
    ) { }
    canActivate(): boolean {
        console.log('UserRouteAccessService');
        if (!this.authJWT.isTokenExpired()) {
            return true;
        }
        this.openSignin();
        return false;
    }

    openSignin(): void {
        const currentModal = this.modal.open(SignInComponent, {container: 'body', size: 'lg', centered: true});
    }
}
