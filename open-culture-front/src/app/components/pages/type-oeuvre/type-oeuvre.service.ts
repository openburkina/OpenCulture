import { Injectable } from '@angular/core';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { TypeOeuvreDTO } from '../../models/type-oeuvre.model';

type EntityTypeOeuvre = HttpResponse<TypeOeuvreDTO>;
type EntityArrayTypeOeuvre = HttpResponse<TypeOeuvreDTO[]>;

@Injectable({
  providedIn: 'root'
})
export class TypeOeuvreService {

  public resourceUrl = environment.apiUrl + 'type-oeuvres';

  constructor(protected httpClient: HttpClient) { }

  findAll(typeOeuvre: string): Observable<EntityArrayTypeOeuvre> {
      return this.httpClient.get< TypeOeuvreDTO[]>(this.resourceUrl,{observe: 'response'});
  }

  findOne(id: number): Observable<EntityTypeOeuvre> {
    return this.httpClient.get< TypeOeuvreDTO>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  create(typeOeuvre:  TypeOeuvreDTO): Observable<EntityTypeOeuvre> {
    return this.httpClient.post< TypeOeuvreDTO>(this.resourceUrl,typeOeuvre,{observe: 'response'});
  }

  update(typeOeuvre:  TypeOeuvreDTO): Observable<EntityTypeOeuvre> {
    return this.httpClient.put< TypeOeuvreDTO> (this.resourceUrl,typeOeuvre,{observe: 'response'});
  }

  delete(id: number): Observable<EntityTypeOeuvre>{
    return this.httpClient.delete(`${this.resourceUrl}/${id}`,{observe: 'response'});
  }
}
