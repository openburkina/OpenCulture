import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FormBuilder, Validators} from '@angular/forms';
import {User} from '../../models/User';
import {SpinnerService} from '../../services/spinner/spinner.service';
import {ApiService} from '../../services/api/api.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {

    user: User;
    errorMessage = null;
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
  ) { }

  ngOnInit(): void {
      this.user = new User();
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
            console.info('USER ', this.user);
            this.apiService.doInscriptionUser(this.user).subscribe(
                response => {
                    this.spinner.close();
                    if (response.body === null) {
                        this.errorMessage = 'Erreur lors de l\'inscription. Veuillez réessayer !';
                    } else {
                        // this.onLogin();
                    }
                },
                error => {
                   // this.spinner.close();
                    this.errorMessage = error.error.title;
                },
            );
        }

    }
}
