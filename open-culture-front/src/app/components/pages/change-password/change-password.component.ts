import { Component, OnInit } from '@angular/core';
import {FormBuilder, Validators} from "@angular/forms";
import {LoginVM} from "../../models/login-vm";
import {ApiService} from "../../services/api/api.service";
import {LoginService} from "../../services/auth/login.service";
import {SpinnerService} from "../../services/spinner/spinner.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NotifierService} from "angular-notifier";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss'],
})
export class ChangePasswordComponent implements OnInit {
    formChangePassword = this.fb.group({
        username: [null, Validators.required],
        password: [null, Validators.required,Validators.pattern],
        confirmPassword: [null, Validators.required,Validators.pattern],
    });
     errorMessage: string = null;
     key = null;
    private loginVM: LoginVM;
    successMessage= null;
    isSaving: boolean;

  constructor(
      private fb: FormBuilder,
      private apiService: ApiService,
      private loginService: LoginService,
      private spinner: SpinnerService,
      private router: Router,
      private route: ActivatedRoute,
     private notify: NotifierService,
  ) { }

  ngOnInit(): void {
      this.loginVM = new LoginVM();
      this.route.queryParams.subscribe(params => {
          this.key = params['passwordkey'];
          console.info('KEY ',this.key)
      });
      this.isSaving = false;
  }

    onChangePassword(): void {
        this.loginVM.rememberMe = false;
      if (this.key!==null && this.key!==undefined) {
          if (this.formChangePassword.get('password').value !== this.formChangePassword.get('confirmPassword').value) {
              this.showNotification('La confirmation du mot de passe est incorrecte !','error');
             // this.errorMessage = 'La confirmation du mot de passe est incorrecte !';
          } else {
              this.spinner.loading();
              this.loginVM.username = this.key;
              this.loginVM.password = this.formChangePassword.get('password').value;
              this.apiService.changePassword(this.loginVM).subscribe(
                  response => {
                      this.spinner.close();
                      if (response.body === null) {
                         // this.errorMessage = 'Erreur lors de l\'initialisation. Veuillez réessayer !';
                          this.showNotification('Erreur lors de l\'initialisation. Veuillez réessayer !','error');
                      } else {
                          this.isSaving =true;
                         // this.showNotification('Votre mot de passe a ete changer,vous pouvez-vous connecter! !','success');
                           this.successMessage = 'Votre mot de passe a ete changer,vous pouvez-vous connecter!';
                      }
                  },
                  error => {
                      this.spinner.close();
                      this.showNotification(error.error.title,'error');
                     // this.errorMessage = error.error.title;
                  },
              );
          }
      } else {
          this.loginVM.username = this.formChangePassword.get('username').value;
          this.apiService.findByLogin(this.loginVM.username).subscribe(value => {
              if (value.body===null) {
                  this.showNotification('Erreur lors de l\'initialisation. Veuillez réessayer !','error');

                  // this.errorMessage = 'Erreur lors de l\'initialisation. Veuillez réessayer !';
              } else {
                  this.successMessage = 'Un mail vous est envoye pour changer votre mot de passe';
                  this.isSaving = true;
              }
          },
           error1 => {
               this.errorMessage =error1.error.title;
           }
          )
      }
    }
    sendEmail() {
        this.apiService.sendEmail(this.loginVM.username).subscribe(res => {
                if (res.body === null) {
                    this.errorMessage ='Erreur lors du renvoi du l\'Email, Veuillez réessayer'
                } else {
                    this.successMessage = 'Mail renvoye avec succes'
                }
            },
            error => {
                this.errorMessage = error.error.title;

            }
        )

    }
    showNotification(text: string, type: string): void {
        this.notify.notify(type,text);
    }
}
