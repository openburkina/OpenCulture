import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {LoginVM} from '../../models/login-vm';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";
import {SpinnerService} from "../../services/spinner/spinner.service";
import {LoginService} from "../../services/auth/login.service";

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {
    loginVM: LoginVM;
    errorMessage = null;
    formLogin = this.fb.group({
        username: [null, [Validators.required]],
        password: [null, Validators.required],
        rememberMe: [false, Validators.required],
    });

  constructor(
      private loginService: LoginService,
      private spinner: SpinnerService,
      private fb: FormBuilder,
      private router: Router,
      private activeModal: NgbActiveModal,
  ) { }

  ngOnInit(): void {
      this.loginVM = new LoginVM();
  }

    onLogin(): void {
        this.spinner.loading();
        this.errorMessage = null;
        this.loginVM.username = this.formLogin.get('username').value;
        this.loginVM.password = this.formLogin.get('password').value;
        this.loginVM.rememberMe = this.formLogin.get('rememberMe').value;
        console.log(this.loginVM);
        this.loginService.login(this.loginVM).subscribe(
            response => {
                this.spinner.close();
                console.log(response);
                if (response === null) {
                    this.errorMessage = 'Erreur lors de la connexion !';
                } else {
                    this.router.navigate(['/']);
                    this.onDismiss(true);
                }
            },
            error => {
                console.log(error);
                this.spinner.close();
                this.errorMessage = error.error.detail;
            },
        );
    }

    onDismiss(param: any): void {
        this.activeModal.close(param);
    }
}
