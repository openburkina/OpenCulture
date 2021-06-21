import { Injectable } from '@angular/core';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { TypeRegroupementDTO } from '../../models/type-regroupement.model';

type EntityTypeRegroupement = HttpResponse<TypeRegroupementDTO>;
type EntityArrayTypeRegroupement = HttpResponse<TypeRegroupementDTO[]>;

@Injectable({
  providedIn: 'root'
})
export class TypeRegroupementService {

  public resourceUrl = environment.apiUrl + 'type-regroupements';

  constructor(protected httpClient: HttpClient) { }

  findAll(typeRegroupement: string): Observable<EntityArrayTypeRegroupement> {
      return this.httpClient.get< TypeRegroupementDTO[]>(this.resourceUrl,{observe: 'response'});
  }

  findAllSimple(): Observable<EntityArrayTypeRegroupement> {
    return this.httpClient.get< TypeRegroupementDTO[]>(this.resourceUrl,{observe: 'response'});
}

  findOne(id: number): Observable<EntityTypeRegroupement> {
    return this.httpClient.get< TypeRegroupementDTO>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  create(typeRegroupement:  TypeRegroupementDTO): Observable<EntityTypeRegroupement> {
    return this.httpClient.post< TypeRegroupementDTO>(this.resourceUrl,typeRegroupement,{observe: 'response'});
  }

  update(typeRegroupement:  TypeRegroupementDTO): Observable<EntityTypeRegroupement> {
    return this.httpClient.put< TypeRegroupementDTO> (this.resourceUrl,typeRegroupement,{observe: 'response'});
  }

  delete(id: number): Observable<EntityTypeRegroupement>{
    return this.httpClient.delete(`${this.resourceUrl}/${id}`,{observe: 'response'});
  }
}
