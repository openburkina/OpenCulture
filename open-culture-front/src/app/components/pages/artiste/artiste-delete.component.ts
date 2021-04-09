import { Component, OnInit } from '@angular/core';
import { ArtisteService } from "./artiste.service";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ArtisteDTO } from '../../models/artiste.model';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'app-artiste',
  templateUrl: './artiste-delete.component.html',
 // styleUrls: ['./artiste-delete.component.scss']
})
export class ArtisteDeleteComponent implements OnInit {

art: ArtisteDTO;

  constructor(
    private artService: ArtisteService,
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
    this.artService.delete(id).subscribe(
      () => {
        this.showNotification("Suppression réussie","success");
        this.activeModal.dismiss(true);
      },
      () => {
        this.showNotification("Certaines oeuvres sont liées à ce artiste","error")
      });
  }

}
