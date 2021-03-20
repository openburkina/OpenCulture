import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {User} from '../../models/User';
import {environment} from '../../../../environments/environment';
import {Observable} from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class ApiService {

    constructor(
        private http: HttpClient,
    ) { }

    doInscriptionUser(user: User): Observable<HttpResponse<User>> {
        return this.http.post<User>(`${environment.SERVER_URL}/users`, user, {observe: 'response'});
    }
}
