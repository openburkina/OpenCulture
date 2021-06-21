import { Component, OnInit } from '@angular/core';
import { TypeRegroupementDTO } from '../../models/type-regroupement.model';
import { TypeRegroupementEditComponent } from './type-regroupement-edit.component';
import { TypeRegroupementDeleteComponent } from './type-regroupement-delete.component';
import { TypeRegroupementService } from './type-regroupement.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {RegroupementEditComponent} from '../regroupement/regroupement-edit.component';

@Component({
  selector: 'app-type-regroupement',
  templateUrl: './type-regroupement.component.html',
  styleUrls: ['./type-regroupement.component.scss']
})
export class TypeRegroupementComponent implements OnInit {

  typeRegroupements: TypeRegroupementDTO[];
    itemsPerPage: number = 10;
    totalItems: number;
    page: number = 1;
  constructor(
    private typeService: TypeRegroupementService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.typeService.findAll(null).subscribe(
      response => {
          this.typeRegroupements = response.body;
          this.totalItems = this.typeRegroupements.length;
      }
    );
  }

  editer(reg: TypeRegroupementDTO): void{
    const modal = this.modalService.open(TypeRegroupementEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.typeRegroupement = reg;
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )
  }

  delete(reg: TypeRegroupementDTO): void{
    const modal = this.modalService.open(TypeRegroupementDeleteComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
    modal.componentInstance.TypeRegroupementDTO = reg;
    modal.result.then(
      response => {
        if (response === true) {
          this.loadAll();
        }
      }
    )
  }

    create(): void{
        const modal = this.modalService.open(TypeRegroupementEditComponent, {backdrop: 'static', container: 'body', centered: true, size: 'lg'});
        modal.result.then(
            response => {
                if (response === true) {
                    this.loadAll();
                }
            }
        )
    }

}
