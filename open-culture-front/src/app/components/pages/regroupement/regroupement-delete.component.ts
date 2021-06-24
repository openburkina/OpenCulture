import { Component, OnInit } from '@angular/core';
import { RegroupementService } from "./regroupement.service";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RegroupementDTO } from '../../models/regroupement.model';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'app-regroupement',
  templateUrl: './regroupement-delete.component.html',
 // styleUrls: ['./regroupement-delete.component.scss']
})
export class RegroupementDeleteComponent implements OnInit {

reg: RegroupementDTO;

  constructor(
    private regService: RegroupementService,
    private notify: NotifierService,
    private activeModal: NgbActiveModal) {
   }

  ngOnInit(): void {
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  showNotification(text: string, type: string): void {
    this.notify.notify(type,text);
  }

  confirmDelete(id: number) {
    this.regService.delete(id).subscribe(
      () => {
        this.showNotification("Suppression réussie","success");
        this.cancel(true);
      },
      () => {
        this.showNotification("Certaines oeuvres sont liées à ce regroupement","error")
      }
    );
  }
    cancel(boolean: boolean): void{
        this.activeModal.close(boolean);
    }

}
