import { Injectable } from '@angular/core';
import { RegroupementDTO } from '../../models/regroupement.model';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

type EntityOeuvre = HttpResponse<RegroupementDTO>;
type EntityArrayOeuvre = HttpResponse<RegroupementDTO[]>;

@Injectable({
  providedIn: 'root'
})
export class RegroupementService {

  public resourceUrl = environment.apiUrl + 'regroupements';

  constructor(protected httpClient: HttpClient) { }

  findAll(reg: string): Observable<EntityArrayOeuvre> {
      return this.httpClient.get<RegroupementDTO[]>(this.resourceUrl,{observe: 'response'});
  }

  findOne(id: number): Observable<EntityOeuvre> {
    return this.httpClient.get<RegroupementDTO>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  create(reg: RegroupementDTO): Observable<EntityOeuvre> {
    return this.httpClient.post<RegroupementDTO>(this.resourceUrl,reg,{observe: 'response'});
  }

  update(reg: RegroupementDTO): Observable<EntityOeuvre> {
    return this.httpClient.put<RegroupementDTO> (this.resourceUrl,reg,{observe: 'response'});
  }

  delete(id: number): Observable<EntityOeuvre>{
    return this.httpClient.delete(`${this.resourceUrl}/${id}`,{observe: 'response'});
  }
}
