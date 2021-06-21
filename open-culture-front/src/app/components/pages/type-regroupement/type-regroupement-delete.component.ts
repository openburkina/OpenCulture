import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TypeRegroupementService } from "./type-regroupement.service";
import { TypeRegroupementDTO } from '../../models/type-regroupement.model';
import { NotifierService } from 'angular-notifier';


@Component({
  selector: 'app-type-regroupement',
  templateUrl: './type-regroupement-delete.component.html',
})
export class TypeRegroupementDeleteComponent implements OnInit {
typeRegroupement: TypeRegroupementDTO;

  constructor(
    private activeModal: NgbActiveModal,
    private notify: NotifierService,
    private typeRegroupementService: TypeRegroupementService) {
  }

 ngOnInit(): void {}

 clear() {
   this.activeModal.dismiss('cancel');
 }

 showNotification(text: string, type: string): void {
  this.notify.notify(type,text);
 }

 confirmDelete(id: number) {
   this.typeRegroupementService.delete(id).subscribe(
    () => {
      this.showNotification("Suppression réussie","success");
      this.cancel(true);

    },
    () => {
      this.showNotification("Certaines oeuvres sont liées à ce type d'oeuvres","error")
    });
 }
    cancel(boolean: boolean): void{
        this.activeModal.close(boolean);
    }

}
