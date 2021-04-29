import { Component, OnInit } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AccountComponent} from '../../pages/account/account.component';
import {SignInComponent} from "../../pages/sign-in/sign-in.component";
import {FormBuilder, Validators} from "@angular/forms";
import {ApiService} from "../../services/api/api.service";
import {LoginService} from "../../services/auth/login.service";
import {User} from "../../models/User";
import {SessionStorageService} from "ngx-webstorage";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
    search :string;
    public user: User;

  constructor(
      private modal: NgbModal,
      private fb: FormBuilder,
      private apiService: ApiService,
      private loginService: LoginService,
      private $sessionStorage: SessionStorageService,
  ) {
      this.user = new User();
  }

  ngOnInit(): void {
      this.user.lastName = this.$sessionStorage.retrieve('lastname');
      this.user.firstName = this.$sessionStorage.retrieve('firstname');
      this.user.login = this.$sessionStorage.retrieve('login');
  }

    openAccount(): void {
         // const currentModal = this.modal.open(AccountComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
        const currentModal = this.modal.open(AccountComponent, { container: 'body', centered: true, size: 'lg'});

    }

    openSignin(): void {
        const currentModal = this.modal.open(SignInComponent, {container: 'body', size: 'lg', centered: true});
    }

    logout(): void {
      this.loginService.logout();
      this.user = null;
    }

}
