import { Component, OnInit } from '@angular/core';
import { OeuvreDTO } from '../../models/oeuvre.model';
import { TypeOeuvreDTO } from '../../models/type-oeuvre.model';
import { OeuvreService } from '../oeuvre/oeuvre.service';
import { TypeOeuvreService } from '../type-oeuvre/type-oeuvre.service';
import { Images } from '../../constant/constant';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OeuvreEditComponent } from '../oeuvre/oeuvre-edit.component';

@Component({
  selector: 'app-entity-blog',
  templateUrl: './entity-blog.component.html',
  styleUrls: ['./entity-blog.component.scss']
})
export class EntityBlogComponent implements OnInit {

  oeuvres: OeuvreDTO[];
  allOeuvres =  new Array()
  categories: TypeOeuvreDTO[];
  categorie: string;
  
  constructor(
    private oeuvreService: OeuvreService,
    private typeOeuvreService: TypeOeuvreService,
    private ngModalService: NgbModal
  ) {
    this.categorie = "Films";
   }

  ngOnInit(): void {
    this.loadAll(); 
  }

  loadAll(){
    this.getOeuvresByTypeOeuvre(this.categorie);
    this.getCategorie();
    this.getAllOeuvre();
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

  getAllOeuvre(){
    let categorie: string;
    this.oeuvreService.findRecentPosts(categorie).subscribe(
      response => {
        this.forRowView(response.body);
      }
    )
  }

  forRowView(oeuvres: OeuvreDTO[]): void{
      const k = 2;
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
          this.getAllOeuvre();
          console.log(this.allOeuvres);
        }
      }
    ).catch(
      data => {
        console.log(data);
      }
    );
  }
  films(){}
  clips(){}
  musiques(){}
  litteratures(){}
  arts(){}

}
