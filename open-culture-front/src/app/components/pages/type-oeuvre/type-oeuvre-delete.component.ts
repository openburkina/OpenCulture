import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TypeOeuvreService } from "./type-oeuvre.service";
import { TypeOeuvreDTO } from '../../models/type-oeuvre.model';
import { NotifierService } from 'angular-notifier';


@Component({
  selector: 'app-type-oeuvre',
  templateUrl: './type-oeuvre-delete.component.html',
 // styleUrls: ['./type-oeuvre-delete.component.scss']
})
export class TypeOeuvreDeleteComponent implements OnInit {
typeOeuvre: TypeOeuvreDTO;

  constructor( 
    private activeModal: NgbActiveModal,
    private notify: NotifierService,
    private typeOeuvreService: TypeOeuvreService) {
  }

 ngOnInit(): void {}

 clear() {
   this.activeModal.dismiss('cancel');
 }

 showNotification(text: string, type: string): void {
  this.notify.notify(type,text);
 }

 confirmDelete(id: number) {
   this.typeOeuvreService.delete(id).subscribe(
    () => {
      this.activeModal.dismiss(true);
      this.showNotification("Suppression réussie","success")
    },
    () => {
      this.showNotification("Certaines oeuvres sont liées à ce type d'oeuvres","error")
    });
 }

}
