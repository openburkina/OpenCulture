import { Injectable } from '@angular/core';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { ArtisteDTO } from '../../models/artiste.model';

type EntityOeuvre = HttpResponse<ArtisteDTO>;
type EntityArrayOeuvre = HttpResponse<ArtisteDTO[]>;

@Injectable({
  providedIn: 'root'
})
export class ArtisteService {

  public resourceUrl = environment.apiUrl + 'artistes';

  constructor(protected httpClient: HttpClient) { }

  findAll(artiste: string): Observable<EntityArrayOeuvre> {
      return this.httpClient.get< ArtisteDTO[]>(this.resourceUrl,{observe: 'response'});
  }

  findOne(id: number): Observable<EntityOeuvre> {
    return this.httpClient.get< ArtisteDTO>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  create(artiste:  ArtisteDTO): Observable<EntityOeuvre> {
    return this.httpClient.post< ArtisteDTO>(this.resourceUrl,artiste,{observe: 'response'});
  }

  update(artiste:  ArtisteDTO): Observable<EntityOeuvre> {
    return this.httpClient.put< ArtisteDTO> (this.resourceUrl,artiste,{observe: 'response'});
  }

  delete(id: number): Observable<EntityOeuvre>{
    return this.httpClient.delete(`${this.resourceUrl}/${id}`,{observe: 'response'});
  }
}
