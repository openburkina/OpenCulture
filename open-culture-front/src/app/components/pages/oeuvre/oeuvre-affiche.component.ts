import {Component, OnInit} from '@angular/core';
import {OeuvreDTO} from '../../models/oeuvre.model';
import {OeuvreService} from './oeuvre.service';
import {TypeFichier} from '../../models/enumeration/type-fichier.enum';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OeuvreEditComponent } from "./oeuvre-edit.component";
import { Router } from '@angular/router';
import { OeuvreDeleteComponent } from './oeuvre-delete.component';

@Component({
  selector: 'app-oeuvre',
  templateUrl: './oeuvre-affiche.component.html',
  styleUrls: ['./oeuvre.component.scss']
})
export class OeuvreAfficheComponent implements OnInit {
  oeuvres:  OeuvreDTO[];
  oeuvresVideo =  new Array();
  oeuvresAudio =  new Array();

  oeuvresView = new Array();
  typeFichier: TypeFichier;
  imagePath = [
      'assets/img/openculture/images-1.jpg',
      'assets/img/openculture/images-2.jpg',
      'assets/img/openculture/images-3.jpg',
      'assets/img/openculture/images-4.jpg',
      'assets/img/openculture/images-5.jpg',
      'assets/img/openculture/images-6.jpg',
      'assets/img/openculture/images-7.jpg',
      'assets/img/openculture/images-8.jpg',
      'assets/img/openculture/images-9.jpg',
      'assets/img/contact/contact-img.png',
  ];
  constructor(
    protected oeuvreService: OeuvreService,
    protected ngModalService: NgbModal,
    protected route: Router
    ) { this.typeFichier = TypeFichier.VIDEO }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.oeuvreService.findComplet().subscribe(
      response => {
          this.oeuvres = response.body;
          console.log(this.oeuvres);
      }
    );
  }

  create(): void{
    const modal = this.ngModalService.open(OeuvreEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
  }

  editer(oeuvreDTO: OeuvreDTO): void{
    console.log(oeuvreDTO);
    const modal = this.ngModalService.open(OeuvreEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.oeuvre = oeuvreDTO;
    
  }

  delete(oeuvreDTO: OeuvreDTO): void{
    const modal = this.ngModalService.open(OeuvreDeleteComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.oeuvre = oeuvreDTO;
    console.log(modal.componentInstance.oeuvreDTO);
  }
}
