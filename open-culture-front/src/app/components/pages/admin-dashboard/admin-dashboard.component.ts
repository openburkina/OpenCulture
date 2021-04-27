import { Component, OnInit } from '@angular/core';
import { OeuvreService } from '../oeuvre/oeuvre.service';
import { OeuvreDTO } from '../../models/oeuvre.model';
import { TypeOeuvreService } from '../type-oeuvre/type-oeuvre.service';
import { TypeOeuvreDTO } from '../../models/type-oeuvre.model';
import { Images } from '../../constant/constant';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {

  oeuvres: OeuvreDTO[];
  categories: TypeOeuvreDTO[];
  categorie: string;

  constructor(
    private oeuvreService: OeuvreService,
    private typeOeuvreService: TypeOeuvreService
  ) {
    this.categorie = "Films";
   }

  ngOnInit(): void {

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

  films(){}
  clips(){}
  musiques(){}
  litteratures(){}
  arts(){}
}
