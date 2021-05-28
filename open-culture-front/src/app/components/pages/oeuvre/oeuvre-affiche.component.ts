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
  typeFichier: TypeFichier;
  constructor(
    protected oeuvreService: OeuvreService,
    protected ngModalService: NgbModal,
    protected route: Router
    ) { this.typeFichier = TypeFichier.VIDEO }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.oeuvreService.findComplet(null).subscribe(
      response => {
          this.oeuvres = response.body;
      }
    );
  }

  create(): void{
    const modal = this.ngModalService.open(OeuvreEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )
  }

  editer(oeuvreDTO: OeuvreDTO): void{
    console.log('OUEVRE',oeuvreDTO);
    const modal = this.ngModalService.open(OeuvreEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.oeuvre = oeuvreDTO;
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )

  }

  delete(oeuvreDTO: OeuvreDTO): void{
    const modal = this.ngModalService.open(OeuvreDeleteComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.oeuvre = oeuvreDTO;
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )
  }
}
