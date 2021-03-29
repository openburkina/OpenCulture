import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TypeOeuvreService } from "./type-oeuvre.service";
import { TypeOeuvreDTO } from '../../models/type-oeuvre.model';


@Component({
  selector: 'app-type-oeuvre',
  templateUrl: './type-oeuvre-delete.component.html',
 // styleUrls: ['./type-oeuvre-delete.component.scss']
})
export class TypeOeuvreDeleteComponent implements OnInit {
typeOeuvre: TypeOeuvreDTO;

  constructor( 
    private activeModal: NgbActiveModal,
    private typeOeuvreService: TypeOeuvreService) {
  }

 ngOnInit(): void {}

 clear() {
   this.activeModal.dismiss('cancel');
 }

 confirmDelete(id: number) {
   this.typeOeuvreService.delete(id).subscribe(() => {
     this.activeModal.dismiss(true);
   });
 }

}
