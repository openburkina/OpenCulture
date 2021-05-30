import { Component, OnInit } from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DetailAbonnementComponent} from "../detail-abonnement/detail-abonnement.component";

@Component({
  selector: 'app-abonnement',
  templateUrl: './abonnement.component.html',
  styleUrls: ['./abonnement.component.scss']
})
export class AbonnementComponent implements OnInit {

  constructor(
      private modal: NgbModal,
  ) { }

  ngOnInit(): void {
  }
    openDtAbonnement(): void {
        const currentModal = this.modal.open(DetailAbonnementComponent, {container: 'body', size: 'lg', backdrop: 'static', centered: true});
    }
}
