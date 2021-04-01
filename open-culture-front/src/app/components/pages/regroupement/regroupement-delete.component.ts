import { Component, OnInit } from '@angular/core';
import { RegroupementService } from "./regroupement.service";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RegroupementDTO } from '../../models/regroupement.model';

@Component({
  selector: 'app-regroupement',
  templateUrl: './regroupement-delete.component.html',
 // styleUrls: ['./regroupement-delete.component.scss']
})
export class RegroupementDeleteComponent implements OnInit {

reg: RegroupementDTO;

  constructor(
    private regService: RegroupementService,
    private activeModal: NgbActiveModal) {
   }

  ngOnInit(): void {
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.regService.delete(id).subscribe(() => {
      this.activeModal.dismiss(true);
    });
  }

}
