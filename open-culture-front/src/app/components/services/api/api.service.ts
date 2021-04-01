import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {User} from '../../models/User';
import {environment} from '../../../../environments/environment';
import {Observable} from 'rxjs';
import {LoginVM} from "../../models/login-vm";

@Injectable({
    providedIn: 'root',
})
export class ApiService {

    constructor(
        private http: HttpClient,
    ) { }

    doInscriptionUser(user: User): Observable<HttpResponse<User>> {
        return this.http.post<User>(`${environment.apiUrl}/users`, user, {observe: 'response'});
    }
    changePassword(loginVM: LoginVM): Observable<HttpResponse<User>> {
        return this.http.post<LoginVM>(`${environment.apiUrl}/account/change-user-password`, loginVM, {observe: 'response'});
    }

    onSearch(search: String): Observable<HttpResponse<any>> {
        return this.http.get(`${environment.apiUrl}/artistes/search/${search}`, {observe: 'response'});
    }
}
