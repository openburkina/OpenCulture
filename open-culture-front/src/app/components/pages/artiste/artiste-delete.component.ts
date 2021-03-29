import { Component, OnInit } from '@angular/core';
import { ArtisteService } from "./artiste.service";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ArtisteDTO } from '../../models/artiste.model';

@Component({
  selector: 'app-artiste',
  templateUrl: './artiste-delete.component.html',
 // styleUrls: ['./artiste-delete.component.scss']
})
export class ArtisteDeleteComponent implements OnInit {

art: ArtisteDTO;

  constructor(
    private artService: ArtisteService,
    private activeModal: NgbActiveModal) {
   }

  ngOnInit(): void {
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.artService.delete(id).subscribe(() => {
      this.activeModal.dismiss(true);
    });
  }

}
