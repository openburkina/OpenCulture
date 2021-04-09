import { Component, OnInit, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { OeuvreDTO } from '../../models/oeuvre.model';
import { OeuvreService } from './oeuvre.service';
import { HttpResponse } from '@angular/common/http';
import { NotifierService } from 'angular-notifier';
type EntityOeuvre = HttpResponse<OeuvreDTO>;
type EntityArrayOeuvre = HttpResponse<OeuvreDTO[]>;

@Component({
  selector: 'app-oeuvre-edit',
  templateUrl: './oeuvre-delete.component.html',
 // styleUrls: ['./oeuvre-delete.component.scss']
})
export class OeuvreDeleteComponent implements OnInit {
oeuvre: OeuvreDTO;


  constructor(
    private regService: OeuvreService,
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
        this.activeModal.dismiss(true);
        this.showNotification("Suppression réussie","success")
      },
      () => {
        this.showNotification("Erreur lors de la suppression","error")
      }  
    );
  }

}
