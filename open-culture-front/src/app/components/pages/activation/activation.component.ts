import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-activation',
  templateUrl: './activation.component.html',
  styleUrls: ['./activation.component.scss']
})
export class ActivationComponent implements OnInit {
    errorMessage: any;
    successMessage: any;
    keyAccount: null;
    keyPassword: null;

  constructor() { }

  ngOnInit(): void {
  }

    activePasswordChange() {

    }

    activeAccount() {

    }
}