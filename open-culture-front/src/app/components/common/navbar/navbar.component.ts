import { Component, OnInit } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AccountComponent} from '../../pages/account/account.component';
import {SignInComponent} from "../../pages/sign-in/sign-in.component";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

  constructor(
      private modal: NgbModal,
  ) { }

  ngOnInit(): void {
  }

    openAccount(): void {
         const currentModal = this.modal.open(AccountComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    }

    openSignin(): void {
        const currentModal = this.modal.open(SignInComponent, {container: 'body', size: 'lg', centered: true});
    }
}
