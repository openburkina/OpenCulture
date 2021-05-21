import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-detail-abonnement',
  templateUrl: './detail-abonnement.component.html',
  styleUrls: ['./detail-abonnement.component.scss']
})
export class DetailAbonnementComponent implements OnInit {

  constructor(
      private activeModal: NgbActiveModal,
  ) { }

  ngOnInit(): void {
  }
    onDismiss(param: boolean) {
        this.activeModal.close(param);
    }
}
