import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {User} from '../../models/User';
import {Observable} from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class ApiService {
    private  SERVER_URL = 'http://localhost:8080/api';

    constructor(
        private http: HttpClient,
    ) { }

    doInscriptionUser(user: User): Observable<HttpResponse<User>> {
        return this.http.post<User>(`${this.SERVER_URL}/users`, user, {observe: 'response'});
    }
}
