import { Component, OnInit } from '@angular/core';
import { Router, NavigationStart, NavigationCancel, NavigationEnd } from '@angular/router';
import { Location, LocationStrategy, PathLocationStrategy } from '@angular/common';
import { filter } from 'rxjs/operators';
import {LoginVM} from "./components/models/login-vm";
import {LoginService} from "./components/services/auth/login.service";
declare let $: any;

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    providers: [
        Location, {
            provide: LocationStrategy,
            useClass: PathLocationStrategy
        }
    ]
})
export class AppComponent implements OnInit {
    location: any;
    routerSubscription: any;
    loginVM: LoginVM;

    constructor(private router: Router,private loginService: LoginService) {
    }

    ngOnInit(){
        this.loginVM = new LoginVM();
        this.recallJsFuntions();
        // this.onLogin();
    }

    recallJsFuntions() {
        this.router.events
        .subscribe((event) => {
            if ( event instanceof NavigationStart ) {
                $('.preloader').fadeIn('slow');
            }
        });
        this.routerSubscription = this.router.events
        .pipe(filter(event => event instanceof NavigationEnd || event instanceof NavigationCancel))
        .subscribe(event => {
            $.getScript('../assets/js/custom.js');
            $('.preloader').fadeOut('slow');
            this.location = this.router.url;
            if (!(event instanceof NavigationEnd)) {
                return;
            }
            window.scrollTo(0, 0);
        });
    }
   /* onLogin(): void {
               this.loginVM.username ='user';
               this.loginVM.password ='user';
               this.loginVM.rememberMe = false;
                this.loginService.login(this.loginVM).subscribe(
            response => {
                    this.router.navigate(['/']);
            },
            error => {
                this.router.navigate(['/']);
            },
        );
    }*/
}
