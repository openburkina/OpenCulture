import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {AbonnementService} from "../../services/abonnement/abonnement.service";
import {ApiService} from "../../services/api/api.service";
import {Abonnement} from "../../models/abonnement";

@Component({
  selector: 'app-detail-abonnement',
  templateUrl: './detail-abonnement.component.html',
  styleUrls: ['./detail-abonnement.component.scss']
})
export class DetailAbonnementComponent implements OnInit {
    moyenPaiement?: string;
    phoneNumber= null;
    abonnement = new Abonnement();
    sendMail?: boolean;
    otp=null;
    successMessage= null;
    errorMessage = null;

  constructor(
      private activeModal: NgbActiveModal,
      private apiService: ApiService,
      private abonnementService: AbonnementService,
  ) { }

  ngOnInit(): void {
      this.sendMail = false;
     this.moyenPaiement = this.abonnementService.getMoyenPaiement()
      console.info('mmmmvv ',this.moyenPaiement)
  }
    onDismiss(param: boolean) {
        this.activeModal.close(param);
    }

    makePaiement() {
        console.info('dfdfdgfd ',this.phoneNumber);
        if (this.otp===null) {
            this.errorMessage='veuillez renseigner votre code de validation';
        } else {
            this.errorMessage=null;
            this.abonnement.phoneNumber = this.phoneNumber;
            this.sendMail==false;
            this.apiService.doAbonnement(this.abonnement).subscribe(value => {
                if (value.body!==null && value.body!==undefined) {
                    console.info('tdsfgfcgsc ',value.body);
                    this.successMessage ='Votre abonnement a ete effectue avec succes'
                }
            })
        }
    }
    doSenMail(){
      if (this.phoneNumber===null){
          this.errorMessage='Veuillez renseigner votre numero de telephone';
      } else {
          this.errorMessage = null;
          this.apiService.sendEmailPaiment().subscribe(value => {
              if (value.body!==null) {
                  this.sendMail = true;
              }
          })
      }
    }
}
