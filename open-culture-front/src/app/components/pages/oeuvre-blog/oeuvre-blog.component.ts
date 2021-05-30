import { Component, OnInit } from '@angular/core';
import { OeuvreDTO } from '../../models/oeuvre.model';
import { TypeOeuvreDTO } from '../../models/type-oeuvre.model';
import { OeuvreService } from '../oeuvre/oeuvre.service';
import { TypeOeuvreService } from '../type-oeuvre/type-oeuvre.service';
import { Images } from '../../constant/constant';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OeuvreEditComponent } from '../oeuvre/oeuvre-edit.component';
import {VgApiService} from '@videogular/ngx-videogular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-entity-blog',
  templateUrl: './oeuvre-blog.component.html',
  styleUrls: ['./oeuvre-blog.component.scss']
})
export class OeuvreBlogComponent implements OnInit {

  oeuvres: OeuvreDTO[];
  test: OeuvreDTO;
  allOeuvres =  new Array();
  categories: TypeOeuvreDTO[];
  categorie: string;
  preload : string = 'auto';
  api : VgApiService;

  constructor(
    private oeuvreService: OeuvreService,
    private typeOeuvreService: TypeOeuvreService,
    private ngModalService: NgbModal,
    private route: Router
  ) {
    this.categorie = "Films";
   }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(){
   this.getOeuvresByTypeOeuvre(this.categorie);
   this.getCategorie();
   this.getAllOeuvre(this.categorie);
  }

  filtreByCategorie(t: TypeOeuvreDTO){
      this.oeuvreService.findComplet(t.intitule).subscribe(
          response => {
              // this.allOeuvres = [];
              this.forRowView(response.body);
          }
      )
  }

  getOeuvresByTypeOeuvre(categorie: string){
    this.oeuvreService.findRecentPosts(categorie).subscribe(
      response => {
        this.oeuvres = response.body;
      //  this.allOeuvres = response.body;
        // for (let i = 0; i < this.oeuvres.length ;i++){
        //   this.oeuvres[i].pathFile = Images[i];
        // }

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

  getAllOeuvre(categorie: string){
    this.oeuvreService.findComplet(categorie).subscribe(
      response => {
        this.forRowView(response.body);
      }
    )
  }

  forRowView(oeuvres: OeuvreDTO[]): void{
      const k = 4;
      let oeuvresAny = new Array();
      for (let i = 0; i < oeuvres.length ;i++){
        oeuvres[i].pathFile = Images[i];
      }

      const length = oeuvres.length;

      for (let i = 0; i < length; i += k ){
        oeuvresAny.push({items: oeuvres.slice(i,i+k)});
      }
      this.allOeuvres = oeuvresAny;
  }

  toCreate(){
    const modal = this.ngModalService.open(OeuvreEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.result.then(
      response => {
       if (response === true) {
          this.loadAll();
        }
      }
    )
  }

  edit(item: OeuvreDTO){
    console.log(item);
    const modal = this.ngModalService.open(OeuvreEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.oeuvre = item;
    modal.result.then(
      response => {
        console.log("modal result");
        console.log(response);
        if (response === true) {
          this.getAllOeuvre(this.categorie);
          console.log(this.allOeuvres);
        }
      }
    ).catch(
      data => {
        console.log(data);
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
    editer(){
        this.oeuvreService.findOne(6901).subscribe(
            response => {
                this.test = response.body;
            }
        );
    }
  films(){}
  clips(){}
  musiques(){}
  litteratures(){}
  arts(){}

    goTo(id: number) {
        this.route.navigate(['/oeuvre-blog-details',id])
    }
}
