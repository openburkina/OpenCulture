import { Component, OnInit } from '@angular/core';
//import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, Validators} from '@angular/forms';
import {User} from '../../models/User';
import {SpinnerService} from '../../services/spinner/spinner.service';
import {ApiService} from '../../services/api/api.service';
import {LoginService} from "../../services/auth/login.service";
import {LoginVM} from "../../models/login-vm";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NotifierService} from "angular-notifier";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {

    user: User;
    errorMessage = null;
    isSaving: boolean;
    isActived: boolean;
    successMessage = null;
    keyActivedAccount = null;
    loginVM: LoginVM;
    formAccount = this.fb.group({
        firstName: [null, [Validators.required,Validators.minLength(2)]],
        lastName: [null, [Validators.required,Validators.minLength(2)]],
        email: [null, [Validators.required, Validators.email]],
        password: [null, [Validators.required,Validators.pattern]],
        telephone: [null, [Validators.required]],
        confirmPassword: [null, [Validators.required,Validators.pattern]],
    });

  constructor(
     // private activeModal: NgbActiveModal,
      private fb: FormBuilder,
      private spinner: SpinnerService,
      private apiService: ApiService,
      private loginService: LoginService,
      private router: Router,
      private route: ActivatedRoute,
     private notify: NotifierService,

  ) {
  }

  ngOnInit(): void {
      this.route.queryParams.subscribe(params => {
          this.keyActivedAccount = params['key'];
      });
      this.isSaving = false;
      this.isActived = false;
      this.user = new User();
      this.loginVM = new LoginVM();
  }

    onDismiss(b: boolean): void {
       // this.activeModal.close(b);
    }

    onAccount(): void {
        if (this.formAccount.get('password').value !== this.formAccount.get('confirmPassword').value) {
            this.showNotification('La confirmation du mot de passe est incorrecte!','error');
            // this.errorMessage = 'La confirmation du mot de passe est incorrecte !';
        } else {
           this.spinner.loading();
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
                   this.spinner.close();
                    if (response.body === null) {
                        this.showNotification('Erreur lors de l\'inscription. Veuillez réessayer !','error');
                        // this.errorMessage = 'Erreur lors de l\'inscription. Veuillez réessayer !';
                    } else {
                        this.isSaving = true;
                        // this.onLogin();
                        this.successMessage = 'Votre compte a été créé veuillez l\'activer a partir de votre Email !';
                    }
                },
                error => {
                    this.spinner.close();
                    this.showNotification('Erreur lors de l\'inscription. Veuillez réessayer !','error');
                    //this.errorMessage = error.error.title;
                },
            );
        }

    }

    /*onLogin(): void {
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
    }*/

    activetAccount(keyActivedAccount: string) {
        this.spinner.loading();
        this.apiService.activateAccount(keyActivedAccount).subscribe(
            response => {
                this.isActived =true;
                this.spinner.close();
                console.info('USER ',response.body);
                if (response.body === null) {
                    this.showNotification('Erreur lors de l\'activation du compte. Veuillez réessayer !','error');
                    // this.errorMessage = 'Erreur lors de l\'activation du compte. Veuillez réessayer !';
                } else {
                    this.successMessage = 'Votre compte a été activer. Veuillez vous connecter !';
                }
            },
            error => {
                this.spinner.close();
                this.showNotification('Erreur lors de l\'activation du compte. Veuillez réessayer !','error');
                //this.errorMessage = error.error.title;
            },
        );

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
