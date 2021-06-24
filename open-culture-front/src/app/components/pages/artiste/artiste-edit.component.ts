import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ArtisteDTO } from '../../models/artiste.model';
import { ArtisteService } from './artiste.service';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NotifierService } from 'angular-notifier';
import { NgbModal, NgbActiveModal, NgbDate } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from "../../common/constant/constant";
import { InformationCivilDTO } from '../../models/infos.model';

type EntityArt = HttpResponse<ArtisteDTO>;

@Component({
  selector: 'app-artiste',
  templateUrl: './artiste-edit.component.html'
 // styleUrls: ['./artiste.component.scss']
})
export class ArtisteEditComponent implements OnInit {
art: ArtisteDTO;
dateNaissance: Date;
  artForm = this.formBuilder.group({
    id: [],
    nom: [],
    prenom: [],
    nationalite: [],
    numeroP: [],
    numeroS: [],
    lieuNaissance: [],
    dateNaissance: []
  })

  constructor(
    private formBuilder: FormBuilder,
    private artService: ArtisteService,
    private notify: NotifierService,
    private ngModal: NgbActiveModal,
    private activatedRoute: ActivatedRoute

  ) { }

  ngOnInit(): void {
    console.log("cool");
    this.activatedRoute.data.subscribe(({ art }) => {
      if (this.art !== undefined && this.art !== null) {
        this.update(this.art);
      }
    });
  }

  public updateSelectedDate(date: NgbDate) {
    // Use this method to set any other date format you want
    this.dateNaissance = new Date(date.year, date.month, date.day);
}

  create(): ArtisteDTO{
    let art = new ArtisteDTO();
    let infos = new InformationCivilDTO();
    art.id = this.artForm.get(['id']).value;
    art.nom = this.artForm.get(['nom']).value;
    art.prenom = this.artForm.get(['prenom']).value;
    infos.nationalite = this.artForm.get(['nationalite']).value;
    infos.numeroP = this.artForm.get(['numeroP']).value;
    infos.numeroS = this.artForm.get(['numeroS']).value;
    infos.lieuNaissance = this.artForm.get(['lieuNaissance']).value;
    infos.dateNaissance = this.artForm.get(['dateNaissance']).value != null ?
                                            moment(this.artForm.get(['dateNaissance']).value, DATE_TIME_FORMAT) : undefined;

    art.informationCivilDTO = infos;

    return art;
  }
  update(art: ArtisteDTO): void{
    this.artForm.patchValue({
      id: art.id,
      nom: art.nom,
      prenom: art.prenom,
      nationalite: art.informationCivilDTO.nationalite,
      numeroP:  art.informationCivilDTO.numeroP,
      numeroS: art.informationCivilDTO.numeroS,
      lieuNaissance: art.informationCivilDTO.lieuNaissance,
      dateNaissance: art.informationCivilDTO.dateNaissance
    });
    console.log(art);
  }

  save(){
    const art = this.create();
    console.log(art.id);
    if(art.nom !== null && art.prenom !== null
       && art.informationCivilDTO.nationalite !== null && art.informationCivilDTO.nationalite !==undefined
       && art.informationCivilDTO.lieuNaissance !== null && art.informationCivilDTO.lieuNaissance !== undefined
       && art.informationCivilDTO.dateNaissance !== null && art.informationCivilDTO.dateNaissance !== undefined){
      if(art.id !== undefined && art.id !== null){
        this.saveState(this.artService.update(art));
      } else {
        this.saveState(this.artService.create(art));
      }
    } else {
      this.showNotification("enregistrement échoué","error");
    }
  }

  saveState(result: Observable<EntityArt>){
    result.subscribe(
      () => {
        this.showNotification("artiste enregistré","success");
        this.cancel(true);
      },
      () => {
        this.showNotification("enregistrement échoué","error");
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
