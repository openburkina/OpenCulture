import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {LocalStorageService} from 'ngx-webstorage';
import {SigninService} from './sign-in.service';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {

    authenticationError: boolean;

    constructor(
        private loginService: SigninService,
        private formBuilder: FormBuilder,
        private router: Router,
        private $localStorage: LocalStorageService
    ){}

    loginForm = this.formBuilder.group({
        username : ['', Validators.required],
        password : ['', Validators.required],
        rememberMe: [false]
    });

    ngOnInit(): void {
        // this.$localStorage.store("test","testtttttttttttttt");
        // console.log(this.$localStorage.retrieve("test"));
    }

    onSubmit(){}


    login() {
        this.loginService.login({
            username: this.loginForm.get('username').value,
            password: this.loginForm.get('password').value,
            rememberMe: false
        }).subscribe(
            () => {
                this.authenticationError = false;
                console.info(this.authenticationError);
                if (this.router.url === '/account/register' || this.router.url.startsWith('/sign-in') || this.router.url.startsWith('/account/reset') || this.router.url.startsWith('/account/activate')){
                    this.router.navigate(['home']);
                }
            },
            () => {
                this.authenticationError = true;
                console.info("book");
                console.info(this.authenticationError);
            }
        );
    }

}
