import { Component, OnInit } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AccountComponent} from '../../pages/account/account.component';
import {SignInComponent} from "../../pages/sign-in/sign-in.component";
import {FormBuilder, Validators} from "@angular/forms";
import {ApiService} from "../../services/api/api.service";
import {Account} from '../../models/account';
import {AccountService} from "../../services/auth/account.service";
import { LoginService } from '../../services/auth/login.service';
import { SessionStorageService } from 'ngx-webstorage';
import { User } from '../../models/User';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
    search :string;
    private account: Account;
    user = new User();

  constructor(
      private modal: NgbModal,
      private fb: FormBuilder,
      private apiService: ApiService,
      private accountService: AccountService,
      private loginService: LoginService,
      private $sessionStorage: SessionStorageService,
  ) { }

    formSearch = this.fb.group({
        search: [null, Validators.required]
    });

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

  /*  onSearch() {
       this.search = this.formSearch.get('search').value;

       this.apiService.onSearch(this.search).subscribe(value => {
           console.info('RESULTAT CHERCHER ',value.body);
       })
    } */

    logout(): void {
      this.loginService.logout();
      this.user = null;
    }
}
