import { Component, OnInit } from '@angular/core';
import { OeuvreDTO } from '../../models/oeuvre.model';
import { TypeOeuvreDTO } from '../../models/type-oeuvre.model';
import { OeuvreService } from '../oeuvre/oeuvre.service';
import { TypeOeuvreService } from '../type-oeuvre/type-oeuvre.service';
import { Images } from '../../constant/constant';
import {ActivatedRoute, Router} from '@angular/router';
import {VgApiService} from '@videogular/ngx-videogular/core';

@Component({
  selector: 'app-entity-blog-details',
  templateUrl: './oeuvre-blog-details.component.html',
  styleUrls: ['./oeuvre-blog-details.component.scss']
})
export class OeuvreBlogDetailsComponent implements OnInit {

  oeuvres: OeuvreDTO[];
  categories: TypeOeuvreDTO[];
  categorie: string;
  oeuvre: OeuvreDTO[] = new Array();
  api: VgApiService;
  url: String;
  constructor(
    private oeuvreService: OeuvreService,
    private typeOeuvreService: TypeOeuvreService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.categorie = "Films";
   }

  ngOnInit(): void {

    const oeuvreId = Number(this.route.snapshot.paramMap.get('id'));
    this.getOeuvre(oeuvreId);
    this.getOeuvresByTypeOeuvre(this.categorie);
    this.getCategorie();

    console.log(this.oeuvre);

  }

  filtreByCategorie(t: TypeOeuvreDTO){
    this.getOeuvresByTypeOeuvre(t.intitule);
  }

  getOeuvresByTypeOeuvre(categorie: string){
    this.oeuvreService.findRecentPosts(categorie).subscribe(
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

  getOeuvre(id: number){
      this.oeuvreService.findOne(id).subscribe(
          resp => {
              this.oeuvre.push(resp.body);
              this.url = this.oeuvre[0].fileUrl;
              console.log(this.oeuvre);
          }
      );
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
      this.router.navigate(['/entity-blog-details',id])
  }
  films(){}
  clips(){}
  musiques(){}
  litteratures(){}
  arts(){}
}
