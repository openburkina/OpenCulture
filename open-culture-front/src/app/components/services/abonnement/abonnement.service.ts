import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AbonnementService {

    moyenPaiement: string;

  constructor() { }

  public setMoyenPaiement(moyenPaiement?: string){
      this.moyenPaiement = moyenPaiement
  }
    public getMoyenPaiement(){
       return  this.moyenPaiement;
    }
}
