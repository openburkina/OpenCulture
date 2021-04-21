import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {LoginVM} from "../../models/login-vm";
import {ApiService} from "../../services/api/api.service";
import {LoginService} from "../../services/auth/login.service";
import {SpinnerService} from "../../services/spinner/spinner.service";
import {Router} from "@angular/router";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss'],
})
export class ChangePasswordComponent implements OnInit {
    formChangePassword = this.fb.group({
        username: [null, Validators.required],
        password: [null, Validators.required],
        confirmPassword: [null, Validators.required],
    });
     errorMessage: string = null;
    private loginVM: LoginVM;
    successMessage= null;

  constructor(
      private fb: FormBuilder,
      private apiService: ApiService,
      private loginService: LoginService,
      private spinner: SpinnerService,
      private router: Router,
      private activeModal: NgbActiveModal,
  ) { }

  ngOnInit(): void {
      this.loginVM = new LoginVM();
  }

    onChangePassword(): void {
        if (this.formChangePassword.get('password').value !== this.formChangePassword.get('confirmPassword').value) {
            this.errorMessage = 'La confirmation du mot de passe est incorrecte !';
        } else {
             this.spinner.loading();
            this.loginVM.username = this.formChangePassword.get('username').value;
            this.loginVM.password = this.formChangePassword.get('password').value;
           this.loginVM.rememberMe = false;
            this.apiService.changePassword(this.loginVM).subscribe(
                response => {
                    this.spinner.close();
                    if (response.body === null) {
                        this.errorMessage = 'Erreur lors de l\'inscription. Veuillez réessayer !';
                    } else {
                        this.successMessage = 'Votre mot de passe a ete changer, un mail vous es ete envoye pour active votre compte';
                        this.formChangePassword.get('username').setValue(null);
                        this.formChangePassword.get('password').setValue(null);
                        this.formChangePassword.get('confirmPassword').setValue(null);
                       // this.onLogin();
                    }
                },
                error => {
                    this.spinner.close();
                    this.errorMessage = error.error.title;
                },
            );
        }
    }
    onLogin(): void {
        this.spinner.loading();
        this.loginService.login(this.loginVM).subscribe(
            response => {
                this.spinner.close();
                if (response === null) {
                    this.errorMessage = 'Erreur lors de la connexion !';
                } else {
                    this.router.navigate(['/']);
                    this.onDismiss(true);
                }
            },
            error => {
                this.spinner.close();
                this.errorMessage = error.error.title;
                this.router.navigate(['/']);
                this.onDismiss(true);
            },
        );
    }
    onDismiss(b: boolean): void {
        this.activeModal.close(b);
    }
}
