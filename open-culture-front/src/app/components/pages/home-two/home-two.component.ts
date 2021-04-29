import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../services/api/api.service";
import {OeuvreService} from "../oeuvre/oeuvre.service";
import {TypeFichier} from "../../models/enumeration/type-fichier.enum";
import {OeuvreDTO} from "../../models/oeuvre.model";

@Component({
  selector: 'app-home-two',
  templateUrl: './home-two.component.html',
  styleUrls: ['./home-two.component.scss']
})
export class HomeTwoComponent implements OnInit {
    typeFile: any;
    criteria: any;
    typeFichier: TypeFichier;
    oeuvres:  OeuvreDTO[];
    oeuvresVideo =  new Array();
    oeuvresAudio =  new Array();
    private splitChaine: string[];
    private finalCriteria ='';
    location: string;

  constructor(
      private apiService: ApiService,
      private oeuvreService: OeuvreService
  ) {
      this.typeFichier = TypeFichier.VIDEO
  }

  ngOnInit(): void {
      this.loadAll();
  }

    search() {
        if (this.typeFile===null || this.typeFile===undefined) {
            this.typeFile = null;
        }
        if (this.criteria === null|| this.criteria===undefined){
            this.finalCriteria = null;
            console.info('vide');
        }
        if (this.criteria!==null &&this.criteria!==undefined) {
            this.splitChaine = this.criteria.split(" ");
            for (let i = 0; i < this.splitChaine.length; i++){
                if (this.splitChaine[i].length>0){
                    this.finalCriteria =this.finalCriteria+' '+this.splitChaine[i];
                    console.info(this.finalCriteria);
                }
            }
        }
        this.apiService.onSearch(this.finalCriteria,this.typeFile).subscribe(value => {
            console.info('RESULTAT CHERCHER ',value.body);
            this.finalCriteria='';
        })
    }

    loadAll(): void {
        this.oeuvreService.findAll(this.typeFichier).subscribe(
            response => {
                this.oeuvres = response.body;
                this.oeuvreService.forRowView(this.oeuvres,this.typeFichier,this.oeuvres,this.oeuvresVideo,this.oeuvresAudio);
                console.log(this.oeuvres);
            }
        );
    }
}
