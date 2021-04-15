import { Component, OnInit } from '@angular/core';
import { ArtisteDTO } from '../../models/artiste.model';
import { ArtisteService } from './artiste.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ArtisteEditComponent } from './artiste-edit.component';
import { ArtisteDeleteComponent } from './artiste-delete.component';

@Component({
  selector: 'app-artiste',
  templateUrl: './artiste.component.html',
  styleUrls: ['./artiste.component.scss']
})
export class ArtisteComponent implements OnInit {
  arts: ArtisteDTO[];
  
  constructor(
    private artService: ArtisteService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.artService.findAll(null).subscribe(
      response => {
          this.arts = response.body;
      }
    );
  }


  create(): void{
    const modal = this.modalService.open(ArtisteEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
  }
  
  editer(artiste: ArtisteDTO): void{
    console.log(artiste);
    const modal = this.modalService.open(ArtisteEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.art = artiste;
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )
  }

  delete(artiste: ArtisteDTO): void{
    const modal = this.modalService.open(ArtisteDeleteComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.art = artiste;
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )
  }

}
