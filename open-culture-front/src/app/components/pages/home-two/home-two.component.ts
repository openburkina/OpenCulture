import { Component, OnInit } from '@angular/core';
import { TypeFichier } from '../../models/enumeration/type-fichier.enum';
import { OeuvreDTO } from '../../models/oeuvre.model';
import { ApiService } from '../../services/api/api.service';
import { OeuvreService } from '../oeuvre/oeuvre.service';

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
      if (this.criteria === null|| this.criteria===undefined|| this.criteria===''){
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
        this.oeuvres = value.body;
        this.oeuvresVideo = [];
        this.oeuvreService.forRowView(3,this.oeuvres,this.typeFile,this.oeuvres,this.oeuvresVideo,this.oeuvresAudio);
          console.info('RESULTAT CHERCHER ',this.oeuvres);
          this.finalCriteria='';
      })
  }

  loadAll(): void {
      this.oeuvreService.findAll(this.typeFichier).subscribe(
          response => {
              this.oeuvres = response.body;
              this.oeuvreService.forRowView(5,this.oeuvres,this.typeFichier,this.oeuvres,this.oeuvresVideo,this.oeuvresAudio);
              console.log(this.oeuvres);
          }
      );
  }
}
