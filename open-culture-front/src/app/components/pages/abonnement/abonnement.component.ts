import { Component, OnInit } from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DetailAbonnementComponent} from "../detail-abonnement/detail-abonnement.component";
import {AbonnementService} from "../../services/abonnement/abonnement.service";

@Component({
  selector: 'app-abonnement',
  templateUrl: './abonnement.component.html',
  styleUrls: ['./abonnement.component.scss']
})
export class AbonnementComponent implements OnInit {

  constructor(
      private modal: NgbModal,
      private abonnementService: AbonnementService,
  ) { }

  ngOnInit(): void {
  }
    openDtAbonnement(moyen?: string): void {
        this.abonnementService.setMoyenPaiement(moyen);
        const currentModal = this.modal.open(DetailAbonnementComponent, {container: 'body', size: 'lg', backdrop: 'static', centered: true});
    }
}
