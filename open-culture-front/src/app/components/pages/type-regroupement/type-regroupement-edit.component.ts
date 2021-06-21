import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { TypeRegroupementService } from "./type-regroupement.service";
import { TypeRegroupementDTO } from "../../models/type-regroupement.model";
import { FormBuilder } from '@angular/forms';
import { NotifierService } from 'angular-notifier';
import { Observable } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';

type EntityTypeRegroupement = HttpResponse<TypeRegroupementDTO>;

@Component({
  selector: 'app-type-regroupement',
  templateUrl: './type-regroupement-edit.component.html'
})
export class TypeRegroupementEditComponent implements OnInit {

  typeRegroupement = new TypeRegroupementDTO();

  typeRegroupementForm = this.formBuilder.group({
    id: [],
    intitule: []
  })

  constructor(
    private formBuilder: FormBuilder,
    private typeRegroupementService: TypeRegroupementService,
    private notify: NotifierService,
    private ngModal: NgbActiveModal,
    private activatedRoute: ActivatedRoute

  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeRegroupement }) => {
      if (this.typeRegroupement.id !== null && this.typeRegroupement.id !== undefined) {
        this.update(this.typeRegroupement);
      }

    });
  }

  create(): TypeRegroupementDTO{
    let typeRegroupement = new TypeRegroupementDTO();

    typeRegroupement.intitule = this.typeRegroupementForm.get(['intitule']).value;
    typeRegroupement.id = this.typeRegroupementForm.get(['id']).value;

    return typeRegroupement;
  }
  update(typeRegroupement: TypeRegroupementDTO): void{
    this.typeRegroupementForm.patchValue({
      id: typeRegroupement.id,
      intitule: typeRegroupement.intitule
    })
  }

  save(){
    const reg = this.create();
    if(reg.intitule !== null){
      if(reg.id !== undefined && reg.id !== null){
        this.saveState(this.typeRegroupementService.update(reg));
      } else {
        this.saveState(this.typeRegroupementService.create(reg));
      }
    } else {
      this.showNotification("regroupement echoue","error");
    }
  }

  saveState(result: Observable<EntityTypeRegroupement>){
    result.subscribe(
      () => {
        this.showNotification("Type oeuvre enregistree","success");
        this.cancel(true);
      },
      () => {
        this.showNotification("Type oeuvre echoue","error");
      }
    );
  }

  showNotification(text: string, type: string): void {
    this.notify.notify(type,text);
  }

  cancel(boolean: boolean): void{
    this.ngModal.close(boolean);
  }

}
