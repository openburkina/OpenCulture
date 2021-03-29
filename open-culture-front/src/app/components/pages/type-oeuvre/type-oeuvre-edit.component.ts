import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { TypeOeuvreService } from "./type-oeuvre.service";
import { TypeOeuvreDTO } from "../../models/type-oeuvre.model";
import { FormBuilder } from '@angular/forms';
import { NotifierService } from 'angular-notifier';
import { Observable } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';

type EntityTypeOeuvre = HttpResponse<TypeOeuvreDTO>;

@Component({
  selector: 'app-type-oeuvre',
  templateUrl: './type-oeuvre-edit.component.html',
  //styleUrls: ['./type-oeuvre.component.scss']
})
export class TypeOeuvreEditComponent implements OnInit {

  typeOeuvre: TypeOeuvreDTO;

  typeOeuvreForm = this.formBuilder.group({
    id: [],
    intitule: []
  })

  constructor(
    private formBuilder: FormBuilder,
    private typeOeuvreService: TypeOeuvreService,
    private notify: NotifierService,
    private ngModal: NgbActiveModal,
    private activatedRoute: ActivatedRoute

  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeOeuvre }) => {
      this.update(this.typeOeuvre);
    });
  }

  create(): TypeOeuvreDTO{
    let typeOeuvre = new TypeOeuvreDTO();

    typeOeuvre.intitule = this.typeOeuvreForm.get(['intitule']).value;
    typeOeuvre.id = this.typeOeuvreForm.get(['id']).value;

    return typeOeuvre;
  }
  update(typeOeuvre: TypeOeuvreDTO): void{
    this.typeOeuvreForm.patchValue({
      id: typeOeuvre.id,
      intitule: typeOeuvre.intitule
    })
  }

  save(){
    const reg = this.create();
    if(reg.intitule !== null){
      if(reg.id !== undefined && reg.id !== null){
        this.saveState(this.typeOeuvreService.update(reg));
      } else {
        this.saveState(this.typeOeuvreService.create(reg));
      }
    } else {
      this.showNotification("regroupement echoue","error");
    }
  }
  
  saveState(result: Observable<EntityTypeOeuvre>){
    result.subscribe(
      () => {
        this.showNotification("Type oeuvre enregistree","success");
        this.cancel(true);
        window.history.back();
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
