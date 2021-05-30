import { Component, OnInit } from '@angular/core';
import { OeuvreDTO } from '../../models/oeuvre.model';
import { TypeFichier } from '../../models/enumeration/type-fichier.enum';
import { OeuvreService } from '../oeuvre/oeuvre.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SignInComponent } from '../sign-in/sign-in.component';
import {ApiService} from "../../services/api/api.service";
import {AccountComponent} from "../account/account.component";
import {VgApiService} from '@videogular/ngx-videogular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home-one',
  templateUrl: './home-one.component.html',
  styleUrls: ['./home-one.component.scss']
})
export class HomeOneComponent implements OnInit {

  oeuvres:  OeuvreDTO[];
  oeuvresVideo =  new Array();
  oeuvresAudio =  new Array();
  oeuvresView = new Array();
  typeFichier: TypeFichier;
  criteria: any;
  typeFile: any;
  splitChaine: string[];
  finalCriteria ='';
  api : VgApiService;

  constructor(
      protected oeuvreService: OeuvreService,
      private modal: NgbModal,
      private apiService: ApiService,
      private route: Router
      ) {
    this.typeFichier = TypeFichier.VIDEO
   }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.oeuvreService.findAll(this.typeFichier).subscribe(
      response => {
        if (response.body.length>0) {
          this.oeuvres = response.body;
          this.oeuvreService.forRowView(4,this.oeuvres,this.typeFichier,this.oeuvres,this.oeuvresVideo,this.oeuvresAudio);
          console.log(this.oeuvres);
        }
      }
    );
  }
  openSignin(): void {
    const currentModal = this.modal.open(SignInComponent, {container: 'body', size: 'lg', centered: true});
  }

  search() {
    if (this.typeFile===null || this.typeFile===undefined) {
        this.typeFile = null;
    }
    if (this.criteria === null|| this.criteria===undefined){
        this.finalCriteria = null;
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
              console.info('TYPE FICHIER ',this.typeFile);
              this.oeuvres = value.body;
              this.oeuvresVideo = [];
              console.info('OEUVRE  ',this.oeuvres);
              this.oeuvreService.forRowView(4,this.oeuvres,this.typeFile,this.oeuvres,this.oeuvresVideo,this.oeuvresAudio);

          this.finalCriteria='';
      })

  }

  openAccount(): void {
    const currentModal = this.modal.open(AccountComponent, {container: 'body', size: 'lg', centered: true});
  }

    onPlayerReady(api: VgApiService) {
        this.api = api;

        this.api.getDefaultMedia().subscriptions.ended.subscribe(
            () => {
                // Set the video to the beginning
                this.api.getDefaultMedia().currentTime = 0;
            }
        );
    }

    goTo(id: number) {
        this.route.navigate(['/oeuvre-blog-details',id])
    }
}
