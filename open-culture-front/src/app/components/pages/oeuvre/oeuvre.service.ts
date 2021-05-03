import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OeuvreDTO } from "../../models/oeuvre.model";
import {TypeFichier} from '../../models/enumeration/type-fichier.enum';
import * as moment from 'moment';
import { map } from 'rxjs/operators';
import { Images } from "../../constant/constant";

type EntityOeuvre = HttpResponse<OeuvreDTO>;
type EntityArrayOeuvre = HttpResponse<OeuvreDTO[]>;

@Injectable({
  providedIn: 'root'
})
export class OeuvreService {
  public resourceUrl = environment.apiUrl + 'oeuvres';

  constructor(protected httpClient: HttpClient) { }

  findAll(typeFichier: string): Observable<EntityArrayOeuvre> {
      return this.httpClient.get<OeuvreDTO[]>(`${this.resourceUrl+'/filter'}/${typeFichier}`,{observe: 'response'})
      .pipe(map((data: EntityArrayOeuvre) => (this.convertDateInArrayOeuvreToClient(data))));
  }

  findComplet(): Observable<EntityArrayOeuvre> {
      let url = this.resourceUrl+'-for-gestionnaire';
    return this.httpClient.get<OeuvreDTO[]>(url,{observe: 'response'})
    .pipe(map((data: EntityArrayOeuvre) => (this.convertDateInArrayOeuvreToClient(data))));
  }

  findOne(id: number): Observable<EntityOeuvre> {
    return this.httpClient.get<OeuvreDTO>(`${this.resourceUrl}/${id}`, { observe: 'response' })
    .pipe(map((oeuvre: EntityOeuvre) => (this.convertDateOeuvreToClient(oeuvre))));
  }

  create(oeuvre: OeuvreDTO): Observable<EntityOeuvre> {
    const data = this.convertDateOeuvreToServer(oeuvre);
    return this.httpClient.post<OeuvreDTO>(this.resourceUrl,data,{observe: 'response'});
  }

  update(oeuvre: OeuvreDTO): Observable<EntityOeuvre> {
    const data = this.convertDateOeuvreToServer(oeuvre);
    return this.httpClient.put<OeuvreDTO> (this.resourceUrl,data,{observe: 'response'})
    .pipe(map((data: EntityOeuvre) => (this.convertDateOeuvreToClient(data))));
  }

  delete(id: number): Observable<EntityOeuvre>{
    return this.httpClient.delete(`${this.resourceUrl}/${id}`,{observe: 'response'});
  }

  convertDateInArrayOeuvreToClient(data: EntityArrayOeuvre): EntityArrayOeuvre {
    if (data.body) {
       data.body.forEach((oeuvre: OeuvreDTO) => {
        oeuvre.dateSortie = oeuvre.dateSortie != null ? moment(oeuvre.dateSortie) : null;
       })
    }
    return data;
  }

  convertDateOeuvreToClient(data: EntityOeuvre): EntityOeuvre {
    if (data.body) {
       data.body.dateSortie = data.body.dateSortie != null ? moment(data.body.dateSortie) : null;
    }
    return data;
  }

  convertDateOeuvreToServer(data: OeuvreDTO): OeuvreDTO {
    const oeuvre = Object.assign(
      {},
      data,
      {
        dateSortie: data.dateSortie !== null && data.dateSortie.isValid() ? data.dateSortie.toJSON() : null
      }
    );
    return oeuvre;
  }

    forRowView(kk: number,tab: OeuvreDTO[],typeFichier: TypeFichier,oeuvres: OeuvreDTO[], oeuvresVideo: Array<any>, oeuvresAudio: Array<any>): void{
    if(oeuvres.length === 0)
      oeuvresVideo = oeuvresAudio = [];
    else {
      const k = kk;
      for (let i = 0; i < tab.length ;i++){
          tab[i].pathFile = Images[i];
      }
      for (let i = 0; i < tab.length; i += k ){
            if(typeFichier == TypeFichier.VIDEO)
               oeuvresVideo.push({items: oeuvres.slice(i,i+k)});

            else if (typeFichier == TypeFichier.AUDIO)
                oeuvresAudio.push({items: oeuvres.slice(i,i+k)});
      }
    }  
  }

  findMyRecentPosts(categorie: string): Observable<EntityArrayOeuvre> {
    let url = this.resourceUrl+'/my-recent-post-oeuvres';
    let params = new HttpParams();
    params = params.append("categorie",categorie);
  return this.httpClient.get<OeuvreDTO[]>(url,{ params: params, observe: 'response'})
  .pipe(map((data: EntityArrayOeuvre) => (this.convertDateInArrayOeuvreToClient(data))));
  }

  findRecentPosts(categorie: string): Observable<EntityArrayOeuvre> {
    let url = this.resourceUrl+'/recent-post-oeuvres';
    let params = new HttpParams();
    params = params.append("categorie",categorie);
  return this.httpClient.get<OeuvreDTO[]>(url,{  params: params, observe: 'response'})
  .pipe(map((data: EntityArrayOeuvre) => (this.convertDateInArrayOeuvreToClient(data))));
  }
}
