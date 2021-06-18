import { Component, OnInit } from '@angular/core';
import { OeuvreService } from '../oeuvre/oeuvre.service';
import { OeuvreDTO } from '../../models/oeuvre.model';
import { TypeOeuvreService } from '../type-oeuvre/type-oeuvre.service';
import { TypeOeuvreDTO } from '../../models/type-oeuvre.model';
import { Images } from '../../constant/constant';
import {VgApiService} from '@videogular/ngx-videogular/core';
import {Router} from '@angular/router';
import {SignInComponent} from "../sign-in/sign-in.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {LocalStorageService, SessionStorageService} from "ngx-webstorage";
import {ChangeGestionPasswordComponent} from "../change-gestion-password/change-gestion-password.component";

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {

  oeuvres: OeuvreDTO[];
  categories: TypeOeuvreDTO[];
  categorie: string;
  oeuvre: OeuvreDTO[] = new Array();
  api: VgApiService;
  menuItem: boolean;

  constructor(
    private oeuvreService: OeuvreService,
    private typeOeuvreService: TypeOeuvreService,
    private route: Router,
    private modal: NgbModal,
    private $localStorage: LocalStorageService,
    private $sessionStorage: SessionStorageService,
  ) {
    this.categorie = "Films";
   }

  ngOnInit(): void {
    this.menuItem = false;
    this.getOeuvresByTypeOeuvre(this.categorie);
    this.getCategorie();

  }

  filtreByCategorie(t: TypeOeuvreDTO){
    this.getOeuvresByTypeOeuvre(t.intitule);
  }

  getOeuvresByTypeOeuvre(categorie: string){
    this.oeuvreService.findMyRecentPosts(categorie).subscribe(
      response => {
        this.oeuvres = response.body;
        for (let i = 0; i < this.oeuvres.length ;i++){
          this.oeuvres[i].pathFile = Images[i];
        }
          this.oeuvre.push(this.oeuvres[0]);
          console.log(this.oeuvre);
      }
    )
  }

  getCategorie(){
    this.typeOeuvreService.findAllSimple().subscribe(
      response => {
        this.categories = response.body;
      }
    )
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

  itemChange(){
      this.menuItem = !this.menuItem;
  }
  clips(){}
  musiques(){}
  litteratures(){}
  arts(){}

    login() {
        this.modal.open(ChangeGestionPasswordComponent, {container: 'body', size: 'lg', backdrop:'static', centered: true});
    }
}
