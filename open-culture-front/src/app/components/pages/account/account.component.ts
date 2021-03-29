import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, Validators} from '@angular/forms';
import {User} from '../../models/User';
import {SpinnerService} from '../../services/spinner/spinner.service';
import {ApiService} from '../../services/api/api.service';
import {LoginService} from "../../services/auth/login.service";
import {LoginVM} from "../../models/login-vm";
import {Router} from "@angular/router";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {

    user: User;
    errorMessage = null;
    loginVM: LoginVM;
    formAccount = this.fb.group({
        firstName: [null, Validators.required],
        lastName: [null, Validators.required],
        email: [null, [Validators.required, Validators.email]],
        password: [null, Validators.required],
        telephone: [null, Validators.required],
        confirmPassword: [null, Validators.required],
    });

  constructor(
      private activeModal: NgbActiveModal,
      private fb: FormBuilder,
      private spinner: SpinnerService,
      private apiService: ApiService,
      private loginService: LoginService,
      private router: Router,
  ) { }

  ngOnInit(): void {
      this.user = new User();
      this.loginVM = new LoginVM();
  }

    onDismiss(b: boolean): void {
        this.activeModal.close(b);
    }

    onAccount(): void {
        if (this.formAccount.get('password').value !== this.formAccount.get('confirmPassword').value) {
            this.errorMessage = 'La confirmation du mot de passe est incorrecte !';
        } else {
           // this.spinner.loading();
            this.user.firstName = this.formAccount.get('firstName').value;
            this.user.lastName = this.formAccount.get('lastName').value;
            this.user.email = this.formAccount.get('email').value;
            this.user.telephone = this.formAccount.get('telephone').value;
            this.user.password = this.formAccount.get('password').value;
            this.user.login = this.formAccount.get('email').value;
            this.loginVM.username = this.user.login;
            this.loginVM.password = this.user.password;
            this.apiService.doInscriptionUser(this.user).subscribe(
                response => {
                  //  this.spinner.close();
                    if (response.body === null) {
                        this.errorMessage = 'Erreur lors de l\'inscription. Veuillez réessayer !';
                    } else {
                         this.onLogin();
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
}
