import { Component, OnInit } from '@angular/core';
import { TypeOeuvreDTO } from '../../models/type-oeuvre.model';
import { TypeOeuvreEditComponent } from './type-oeuvre-edit.component';
import { TypeOeuvreDeleteComponent } from './type-oeuvre-delete.component';
import { TypeOeuvreService } from './type-oeuvre.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-type-oeuvre',
  templateUrl: './type-oeuvre.component.html',
  styleUrls: ['./type-oeuvre.component.scss']
})
export class TypeOeuvreComponent implements OnInit {

  typeOeuvres: TypeOeuvreDTO[];
  constructor(
    private typeService: TypeOeuvreService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.typeService.findAll(null).subscribe(
      response => {
          this.typeOeuvres = response.body;
      }
    );
  }

  editer(reg: TypeOeuvreDTO): void{
    const modal = this.modalService.open(TypeOeuvreEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.typeOeuvre = reg;
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )
  }

  delete(reg: TypeOeuvreDTO): void{
    const modal = this.modalService.open(TypeOeuvreDeleteComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.typeOeuvre = reg;
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )
  }

}
