import {Component, OnInit, ElementRef, ViewEncapsulation} from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder } from '@angular/forms';
import { OeuvreDTO } from '../../models/oeuvre.model';
import { OeuvreService } from './oeuvre.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { NotifierService } from 'angular-notifier';
import { ArtisteDTO } from '../../models/artiste.model';
import { TypeOeuvreDTO } from '../../models/type-oeuvre.model';
import { RegroupementDTO } from '../../models/regroupement.model';
import { RegroupementService } from "../regroupement/regroupement.service";
import { TypeOeuvreService } from "../type-oeuvre/type-oeuvre.service";
import { ArtisteService } from "../artiste/artiste.service";
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from "../../common/constant/constant";
import { ActivatedRoute } from '@angular/router';
import {IDropdownSettings} from 'ng-multiselect-dropdown';

type EntityOeuvre = HttpResponse<OeuvreDTO>;
type EntityArrayOeuvre = HttpResponse<OeuvreDTO[]>;

@Component({
  selector: 'app-oeuvre-edit',
  templateUrl: './oeuvre-edit.component.html',
  styleUrls: ['./oeuvre-edit.component.scss'],
    encapsulation: ViewEncapsulation.None
})
export class OeuvreEditComponent implements OnInit {
  oeuvre = new OeuvreDTO();
  oeuvreForm = this.formBuilder.group({
    id: [],
    titre: [],
    resume: [],
    typeOeuvreId: [],
    regroupementId: [],
    dateSortie: [],
    fileContent: [],
    fileExtension: [],
    filePath: [],
    fileUrl: [],
    file: [],
    artisteSelected: [],
    nomArtiste: []

  });

  artiste: ArtisteDTO[];
  regroupement: RegroupementDTO[];
  typeOeuvre: TypeOeuvreDTO[];
  artisteSelected : ArtisteDTO[];
  dropdownSettings : IDropdownSettings;
  file: File;

  constructor(
    private ngModal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private oeuvreService: OeuvreService,
    private notify: NotifierService,
    protected elementRef: ElementRef,
    protected regService: RegroupementService,
    protected typeOeuvreService: TypeOeuvreService,
    protected artisteService: ArtisteService,
    protected activatedRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ oeuvre }) => {
      if (this.oeuvre.id !== null && this.oeuvre.id !== undefined) {
        console.log("this.artisteSelected");
        this.updateOeuvre(this.oeuvre);
        this.artisteSelected = this.oeuvre.artistes;
        this.oeuvreForm.patchValue(
        {
          artisteSelected: this.artisteSelected
        }
    )
      }
    });
    this.regService.findAll("album").subscribe(
      resp => {
          this.regroupement = resp.body;
      }
    );
    this.typeOeuvreService.findAll("musique").subscribe(
      resp => {
        this.typeOeuvre = resp.body;
      }
    );
    this.artisteService.findAll("").subscribe(
      resp => {
        this.artiste = resp.body;
      }
    );
     this.dropdownSettings = {
          singleSelection: false,
          idField: 'id',
          textField: 'nom',
          enableCheckAll: false,
          selectAllText: 'Select All',
          unSelectAllText: 'UnSelect All',
          itemsShowLimit: 3,
          allowSearchFilter: true
      };
  }

  createOeuvre(): OeuvreDTO {
    let oeuvre = new OeuvreDTO();

    oeuvre.id = this.oeuvreForm.get(['id']).value;
    oeuvre.dateSortie = this.oeuvreForm.get(['dateSortie']).value != null ?
                              moment(this.oeuvreForm.get(['dateSortie']).value, DATE_TIME_FORMAT) : undefined;
    oeuvre.fileExtension = this.oeuvreForm.get(['fileExtension']).value;
    oeuvre.fileContent = this.oeuvreForm.get(['fileContent']).value;
    oeuvre.file = this.oeuvreForm.get(['fileUrl']).value;
    oeuvre.resume = this.oeuvreForm.get(['resume']).value;
    oeuvre.titre = this.oeuvreForm.get(['titre']).value;
    oeuvre.typeOeuvreId = this.oeuvreForm.get(['typeOeuvreId']).value;
    oeuvre.regroupementId = this.oeuvreForm.get(['regroupementId']).value;
    oeuvre.artistes = this.oeuvreForm.get(['artisteSelected']).value;

    return oeuvre;
  }

  updateOeuvre(oeuvre: OeuvreDTO): void{
    this.oeuvreForm.patchValue({
      id: oeuvre.id,
      titre: oeuvre.titre,
      resume: oeuvre.resume,
      regroupementId: oeuvre.regroupementDTO.id,
      typeOeuvreId: oeuvre.typeOeuvreDTO.id,
      fileContent: oeuvre.fileContent,
      fileExtension: oeuvre.fileExtension,
      file: oeuvre.fileUrl,
      dateSortie: oeuvre.dateSortie != null ? oeuvre.dateSortie.format(DATE_TIME_FORMAT) : null,
      artisteSelected: this.artisteSelected
   //   artisteId: oeuvre.artisteId,
    })
  }

  save(){
    const oeuvre = this.createOeuvre();
    console.log(oeuvre);
    if (this.validateOeuvre(oeuvre)) {
      if(oeuvre.id !== undefined && oeuvre.id !== null){
        this.saveState(this.oeuvreService.update(oeuvre));
      } else {
        this.saveState(this.oeuvreService.create(oeuvre));

      }
    } else {
      return this.showNotification("Il manque certains paramètres","error");
    }

  }

  validateOeuvre(oeuvre: OeuvreDTO): Boolean{
    if(oeuvre.titre.length > 0 &&
     //  oeuvre.fileContent != null &&
     //  oeuvre.fileExtension != null &&
       oeuvre.artistes != null &&
       oeuvre.regroupementId != null &&
       oeuvre.typeOeuvreId != null &&
       oeuvre.dateSortie != null)
      return true;
    else
      return false;
  }

  saveState(result: Observable<EntityOeuvre>){
    result.subscribe(
      () => {
        this.showNotification("oeuvre enregistrée","success");
        this.cancel(true);
      },
      () => {
        this.showNotification("enregistrement echoué","error");
      }
    );
  }

  cancel(boolean: boolean): void{
      this.ngModal.close(boolean);
  }

  ok(): void{
    this.showNotification("oeuvre enregistree","error");
  }
  showNotification(text: string, type: string): void {
    this.notify.notify(type,text);
  }

  trackArtisteById(index: number, item: ArtisteDTO) {
    return item.id;
  }

  trackTypeOeuvreById(index: number, item: TypeOeuvreDTO) {
    return item.id;
  }

  trackRegroupementById(index: number, item: RegroupementDTO) {
    return item.id;
  }

  onFileChange(event) {
      if (event.target.files.length>0){
          this.file = event.target.files[0];
          console.log(this.file);
          this.oeuvreForm.patchValue({
              file: this.file
          });
      }
  }

    sa(){
        const oeuvre = this.file;
        console.log(oeuvre);
        this.saveState(this.oeuvreService.creat(oeuvre));
    }
}
