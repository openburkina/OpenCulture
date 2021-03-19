import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OeuvreDTO } from "../../models/oeuvre.model";
import {TypeFichier} from '../../models/enumeration/type-fichier.enum';

type EntityOeuvre = HttpResponse<OeuvreDTO>;
type EntityArrayOeuvre = HttpResponse<OeuvreDTO[]>;

@Injectable({
  providedIn: 'root'
})
export class OeuvreService {
  public resourceUrl = environment.apiUrl + 'oeuvres';

  constructor(protected httpClient: HttpClient) { }

  findAll(typeFichier: string): Observable<EntityArrayOeuvre> {
      return this.httpClient.get<OeuvreDTO[]>(`${this.resourceUrl+'/filter'}/${typeFichier}`,{observe: 'response'});
  }

  findOne(id: number): Observable<EntityOeuvre> {
    return this.httpClient.get<OeuvreDTO>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  create(oeuvre: OeuvreDTO): Observable<EntityOeuvre> {
    return this.httpClient.post<OeuvreDTO>(this.resourceUrl,oeuvre,{observe: 'response'});
  }

  update(oeuvre: OeuvreDTO): Observable<EntityOeuvre> {
    return this.httpClient.put<OeuvreDTO> (this.resourceUrl,oeuvre,{observe: 'response'});
  }

  delete(id: number): Observable<EntityOeuvre>{
    return this.httpClient.delete(`${this.resourceUrl}/${id}`,{observe: 'response'});
  }
}
