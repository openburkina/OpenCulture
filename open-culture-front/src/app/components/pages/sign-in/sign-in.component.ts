import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {LoginVM} from '../../models/login-vm';
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {Router} from "@angular/router";
import {SpinnerService} from "../../services/spinner/spinner.service";
import {LoginService} from "../../services/auth/login.service";
import {ChangePasswordComponent} from "../change-password/change-password.component";
import {AccountService} from "../../services/auth/account.service";
import {User} from "../../models/User";

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.scss']
})
export class SignInComponent implements OnInit {
    loginVM: LoginVM;
    account: User;
    errorMessage = null;
    formLogin = this.fb.group({
        username: [null, [Validators.required]],
        password: [null, Validators.required],
        rememberMe: [false, Validators.required],
    });
    location: string;

  constructor(
      private loginService: LoginService,
      private accountService: AccountService,
      private spinner: SpinnerService,
      private fb: FormBuilder,
      private router: Router,
      private activeModal: NgbActiveModal,
      private modal: NgbModal,
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
                if (response === null) {
                    this.errorMessage = 'Erreur lors de la connexion !';
                } else if (response.authorities.some(roles => roles ==='ROLE_ADMIN')){
                    this.router.navigate(['/admin-dashboard']);
                    this.onDismiss(true);
                } else {
                    console.info('USER ',response);
                    this.router.navigate(['/dashboard']);
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

    changePassword() {
       this.onDismiss(false);
        const currentModal = this.modal.open(ChangePasswordComponent, {container: 'body', size: 'lg', centered: true});
    }

    close() {

    }
}
