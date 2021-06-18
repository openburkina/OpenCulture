import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, Validators} from "@angular/forms";
import {Password} from "../../models/Password";
import {ApiService} from "../../services/api/api.service";
import {NotifierService} from "angular-notifier";
import {AuthJWTService} from "../../services/auth/auth-jwt.service";

@Component({
  selector: 'app-change-gestion-password',
  templateUrl: './change-gestion-password.component.html',
  styleUrls: ['./change-gestion-password.component.scss']
})
export class ChangeGestionPasswordComponent implements OnInit {
    errorMessage= null;
    successMessage= null;
    password: Password;

    formPassword = this.fb.group({
        oldPassword: [null, []],
        newPassword: [null, [Validators.required,Validators.pattern]],
        confirmNewPsaaword: [null, [Validators.required,Validators.pattern]],
    });

  constructor(
      private activeModal: NgbActiveModal,
      private fb: FormBuilder,
      private apiService: ApiService,
      private notify: NotifierService,
      private authJWTService: AuthJWTService,
  ) { }

  ngOnInit(): void {
      this.password = new Password();
  }
    showNotification(text: string, type: string): void {
        this.notify.notify(type,text);
    }
  onDismiss(param: boolean) {
        this.activeModal.close(param);
    }

    save() {
      if (this.formPassword.get('oldPassword').value===null || this.formPassword.get('oldPassword').value===undefined){
          this.errorMessage='Veuillez renseigner votre ancien mot de passe';
          this.showNotification('Veuillez renseigner votre ancien mot de passe','error');
      }
       else if (this.formPassword.get('newPassword').value!==null &&this.formPassword.get('newPassword').value!==undefined) {
        if (this.formPassword.get('newPassword').value=== this.formPassword.get('confirmNewPsaaword').value) {
           this.password.currentPassword = this.formPassword.get('oldPassword').value;
           this.password.newPassword = this.formPassword.get('newPassword').value;
           this.apiService.doChangePassword(this.password).subscribe(user =>{
               if (user.body!==null &&user.body!==undefined){
                   this.showNotification('Votre mot de passe a été changé avec succès ','success');
                   this.authJWTService.logout();
                   this.onDismiss(false);

               } else {
                   this.showNotification('Impossible de changer le mot de passe ','error');
               }
           },error1 => {
               this.showNotification('Erreur lors de l\'operation','error');
           } )
        } else {
            this.errorMessage='Vos mot de passe ne correspondent pas';
        }
      } else {
          this.errorMessage='Veuillez renseigner votre nouveau mot de passe'
      }
    }
}
