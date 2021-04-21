import {Component, OnInit} from '@angular/core';
import {OeuvreDTO} from '../../models/oeuvre.model';
import {OeuvreService} from './oeuvre.service';
import {TypeFichier} from '../../models/enumeration/type-fichier.enum';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OeuvreEditComponent } from "./oeuvre-edit.component";
import { Router } from '@angular/router';
import { OeuvreDeleteComponent } from './oeuvre-delete.component';
import { locale } from 'moment';

@Component({
  selector: 'app-oeuvre',
  templateUrl: './oeuvre.component.html',
  styleUrls: ['./oeuvre.component.scss']
})
export class OeuvreComponent implements OnInit {
  oeuvres:  OeuvreDTO[];
  oeuvresVideo =  new Array();
  oeuvresAudio =  new Array();
  oeuvresView = new Array();
  typeFichier: TypeFichier;

  constructor(
    protected oeuvreService: OeuvreService,
    private ngModalService: NgbModal,
    protected route: Router
    ) { this.typeFichier = TypeFichier.VIDEO }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.oeuvreService.findAll(this.typeFichier).subscribe(
      response => {
          this.oeuvres = response.body;
          this.oeuvreService.forRowView(response.body,this.typeFichier,this.oeuvres,this.oeuvresVideo,this.oeuvresAudio);
          console.log(this.oeuvres);
      }
    );
  }

  create(): void{
    const modal = this.ngModalService.open(OeuvreEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
  }

  editer(oeuvre: OeuvreDTO) {
    console.log('OUEVRE', oeuvre);
    const modal = this.ngModalService.open(OeuvreEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.oeuvre = oeuvre;
    modal.result.then(
      response => {
        console.log("modal result");
        console.log(response);
        if (response === true) {
          this.loadAll();
        }
      }
    ).catch(
      data => {
        console.log(data);
      }
    );

  }

  delete(oeuvre: OeuvreDTO): void{
    const modal = this.ngModalService.open(OeuvreDeleteComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.oeuvre = oeuvre;
  }
}
