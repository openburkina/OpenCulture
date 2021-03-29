import { Component, OnInit, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators, RequiredValidator } from '@angular/forms';
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
type EntityOeuvre = HttpResponse<OeuvreDTO>;
type EntityArrayOeuvre = HttpResponse<OeuvreDTO[]>;

@Component({
  selector: 'app-oeuvre-edit',
  templateUrl: './oeuvre-edit.component.html',
  styleUrls: ['./oeuvre-edit.component.scss']
})
export class OeuvreEditComponent implements OnInit {
  oeuvre: OeuvreDTO;
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
    artisteId: [],
    nomArtiste: []
    
  });

  artiste: ArtisteDTO[];
  regroupement: RegroupementDTO[];
  typeOeuvre: TypeOeuvreDTO[];

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
      this.updateOeuvre(this.oeuvre);
    })
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
  }

  createOeuvre(): OeuvreDTO {
    let oeuvre = new OeuvreDTO();

    oeuvre.id = this.oeuvreForm.get(['id']).value;
    oeuvre.dateSortie = this.oeuvreForm.get(['dateSortie']).value != null ? 
                              moment(this.oeuvreForm.get(['dateSortie']).value, DATE_TIME_FORMAT) : undefined;
    oeuvre.fileExtension = this.oeuvreForm.get(['fileExtension']).value;
    oeuvre.fileContent = this.oeuvreForm.get(['fileContent']).value;
    oeuvre.resume = this.oeuvreForm.get(['resume']).value;
    oeuvre.titre = this.oeuvreForm.get(['titre']).value;
    oeuvre.typeOeuvreId = this.oeuvreForm.get(['typeOeuvreId']).value;
    oeuvre.regroupementId = this.oeuvreForm.get(['regroupementId']).value;
    oeuvre.artisteId = this.oeuvreForm.get(['artisteId']).value;
    // oeuvre.nomArtiste = this.oeuvreForm.get(['artiste']).value;

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
      dateSortie: oeuvre.dateSortie ? oeuvre.dateSortie.format(DATE_TIME_FORMAT) : undefined,
      artisteId: oeuvre.artisteId,
    //  nomArtiste: oeuvre.nomArtiste
    })
  }

  save(){
    const oeuvre = this.createOeuvre();

    if (this.validateOeuvre(oeuvre)) {
      if(oeuvre.id !== undefined && oeuvre.id !== null){
        console.log("update");
        console.log(oeuvre);
        this.saveState(this.oeuvreService.update(oeuvre));
      } else {
        console.log("create");
        console.log(oeuvre);
        this.saveState(this.oeuvreService.create(oeuvre));

      }
    } else {
      return this.showNotification("Il manque certains paramètres","error");
    }

  }

  validateOeuvre(oeuvre: OeuvreDTO): Boolean{
    console.log(oeuvre.titre);
    console.log(oeuvre.artisteId);
    console.log(oeuvre.regroupementId);
    console.log(oeuvre.dateSortie);
    // console.log(oeuvre.fileContent);
    console.log(oeuvre.fileExtension);
    if(oeuvre.titre != null &&
     //  oeuvre.fileContent != null &&
     //  oeuvre.fileExtension != null &&
       oeuvre.artisteId != null &&
       oeuvre.regroupementId != null && 
       oeuvre.typeOeuvreId != null &&
       oeuvre.dateSortie != null)
      return true;
    else
      return false;  
  }

  setFileData(event, field: string, isImage) {

    return new Promise((resolve, reject) => {
    if (event && event.target && event.target.files && event.target.files[0]) {
      const file: File = event.target.files[0];
      console.log(file.type);
      if (isImage && !file.type.startsWith('image/')) {
        reject(`File was expected to be an image but was found to be ${file.type}`);
      } else {
        const filedContentType: string = field + 'ContentType';
        this.toBase64(file, base64Data => {
          this.oeuvreForm.patchValue({
            [field]: base64Data,
            [filedContentType]: file.type
            
          });
        });
      }
    } else {
      reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
    }
  }).then(
    () => console.log('blob added'), // success
    this.notify.show
  );
}

  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.oeuvreForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

private size(value: string): number {
    return (value.length / 4) * 3 - this.paddingSize(value);
}

private formatAsBytes(size: number): string {
    return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ') + ' bytes';
}

private endsWith(suffix: string, str: string): boolean {
  return str.includes(suffix, str.length - suffix.length);
}

private paddingSize(value: string): number {
  if (this.endsWith('==', value)) {
      return 2;
  }
  if (this.endsWith('=', value)) {
      return 1;
  }
  return 0;
}

    /**
     * Method to find the byte size of the string provides
     */
  byteSize(base64String: string): string {
      return this.formatAsBytes(this.size(base64String));
  }

  /**
   * Method to open file
   */
  openFile(contentType: string, data: string): void {
      if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          // To support IE and Edge
          const byteCharacters = atob(data);
          const byteNumbers = new Array(byteCharacters.length);
          for (let i = 0; i < byteCharacters.length; i++) {
              byteNumbers[i] = byteCharacters.charCodeAt(i);
          }
          const byteArray = new Uint8Array(byteNumbers);
          const blob = new Blob([byteArray], {
              type: contentType
          });
          window.navigator.msSaveOrOpenBlob(blob);
      } else {
          // Other browsers
          const fileURL = `data:${contentType};base64,${data}`;
          const win = window.open();
          win.document.write(
              '<iframe src="' +
                  fileURL +
                  '" frameborder="0" style="border:0; top:0; left:0; bottom:0; right:0; width:100%; height:100%;" allowfullscreen></iframe>'
          );
      }
  }

  toBase64(file: File, cb: Function): void {
    const fileReader: FileReader = new FileReader();
    fileReader.onload = function(e: any) {
        const base64Data: string = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
        cb(base64Data);
    };
    fileReader.readAsDataURL(file);
  }

  saveState(result: Observable<EntityOeuvre>){
    result.subscribe(
      () => {
        this.showNotification("oeuvre enregistree","success");
        this.cancel(true);
      //  window.history.back();
      },
      () => {
        this.showNotification("enregistrement echoue","error");
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

}
