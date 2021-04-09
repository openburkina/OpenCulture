import { Component, OnInit } from '@angular/core';
import { RegroupementDTO } from '../../models/regroupement.model';
import { RegroupementService } from './regroupement.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { RegroupementEditComponent } from './regroupement-edit.component';
import { RegroupementDeleteComponent } from './regroupement-delete.component';

@Component({
  selector: 'app-regroupement',
  templateUrl: './regroupement.component.html',
  styleUrls: ['./regroupement.component.scss']
})
export class RegroupementComponent implements OnInit {

  regs: RegroupementDTO[];
  constructor(
    private regService: RegroupementService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.regService.findAll(null).subscribe(
      response => {
          this.regs = response.body;
      }
    );
  }

  editer(reg: RegroupementDTO): void{
    const modal = this.modalService.open(RegroupementEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.reg = reg;
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )
  }

  delete(reg: RegroupementDTO): void{
    const modal = this.modalService.open(RegroupementDeleteComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.reg = reg;
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )
  }

}
