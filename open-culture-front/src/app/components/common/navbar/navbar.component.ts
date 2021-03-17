import { Component, OnInit } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AccountComponent} from '../../pages/account/account.component';

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

    openAccount() {
         const currentModal = this.modal.open(AccountComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    }
}
