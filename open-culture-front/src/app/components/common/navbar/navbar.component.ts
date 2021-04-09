import { Component, OnInit } from '@angular/core';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AccountComponent} from '../../pages/account/account.component';
import {SignInComponent} from "../../pages/sign-in/sign-in.component";
import {FormBuilder, Validators} from "@angular/forms";
import {ApiService} from "../../services/api/api.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
    search :string;

  constructor(
      private modal: NgbModal,
      private fb: FormBuilder,
      private apiService: ApiService,
  ) { }

    formSearch = this.fb.group({
        search: [null, Validators.required]
    });

  ngOnInit(): void {
  }

    openAccount(): void {
         const currentModal = this.modal.open(AccountComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    }

    openSignin(): void {
        const currentModal = this.modal.open(SignInComponent, {container: 'body', size: 'lg', centered: true});
    }

    onSearch() {
       this.search = this.formSearch.get('search').value;

       this.apiService.onSearch(this.search).subscribe(value => {
           console.info('RESULTAT CHERCHER ',value.body);
       })
    }
}
