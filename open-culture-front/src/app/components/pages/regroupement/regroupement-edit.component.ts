import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { RegroupementDTO } from '../../models/regroupement.model';
import { RegroupementService } from './regroupement.service';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NotifierService } from 'angular-notifier';
import { NgbModal, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import {TypeRegroupementService} from '../type-regroupement/type-regroupement.service';
import {TypeRegroupementDTO} from '../../models/type-regroupement.model';

type EntityReg = HttpResponse<RegroupementDTO>;

@Component({
  selector: 'app-regroupement',
  templateUrl: './regroupement-edit.component.html'
 // styleUrls: ['./regroupement.component.scss']
})
export class RegroupementEditComponent implements OnInit {
reg = new RegroupementDTO();
typeRegroupements: TypeRegroupementDTO[];
  regForm = this.formBuilder.group({
    id: [],
    typeRegroupementId: [],
    intitule: []
  })

  constructor(
    private formBuilder: FormBuilder,
    private regService: RegroupementService,
    private typeRegService: TypeRegroupementService,
    private notify: NotifierService,
    private ngModal: NgbActiveModal,
    private activatedRoute: ActivatedRoute

  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reg }) => {
      if (this.reg.id !== null && this.reg.id !== undefined) {
        this.update(this.reg);
      }
    });
      this.typeRegService.findAll("").subscribe(
          resp => {
              this.typeRegroupements = resp.body;
          }
      );
  }

  create(): RegroupementDTO{
    let reg = new RegroupementDTO();

    reg.id = this.regForm.get(['id']).value;
    reg.typeRegroupementId = this.regForm.get(['typeRegroupementId']).value;
    reg.intitule = this.regForm.get(['intitule']).value;

    return reg;
  }
  update(reg: RegroupementDTO): void{
    this.regForm.patchValue({
      id: reg.id,
      typeRegroupementId: reg.typeRegroupementDTO.id,
      intitule: reg.intitule
    })
  }

  save(){
    const reg = this.create();
    console.log(reg.id);
    if(reg.intitule !== null && reg.typeRegroupementDTO !== null){
      if(reg.id !== undefined && reg.id !== null){
        this.saveState(this.regService.update(reg));
      } else {
        this.saveState(this.regService.create(reg));
      }
    } else {
      this.showNotification("regroupement echoue","error");
    }
  }

  saveState(result: Observable<EntityReg>){
    result.subscribe(
      () => {
        this.showNotification("regroupement enregistree","success");
        this.cancel(true);
      },
      () => {
        this.showNotification("regroupement echoue","error");
      }
    );
  }

  showNotification(text: string, type: string): void {
    this.notify.notify(type,text);
  }

  cancel(boolean: boolean): void{
    this.ngModal.close(boolean);
  }

    trackTypeRegroupementById(index: number, item: RegroupementDTO) {
        return item.id;
    }
}
