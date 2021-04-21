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
        return this.http.post<User>(`${environment.apiUrl}register`, user, {observe: 'response'});
    }


    activateAccount(key : string): Observable<HttpResponse<User>> {
        return this.http.get<User>(`${environment.apiUrl}activate?key=${key}`,{observe: 'response'});
    }

    changePassword(loginVM: LoginVM): Observable<HttpResponse<User>> {
        return this.http.post<LoginVM>(`${environment.apiUrl}account/change-user-password`, loginVM, {observe: 'response'});
    }

    findByLogin(login: string): Observable<HttpResponse<User>> {
        return this.http.post<LoginVM>(`${environment.apiUrl}account/reset-password/init`,login, {observe: 'response'});
    }

    sendEmail(login: string): Observable<HttpResponse<User>> {
        return this.http.post<LoginVM>(`${environment.apiUrl}account/send-email`,login, {observe: 'response'});
    }

    onSearch(search: String): Observable<HttpResponse<any>> {
        return this.http.get(`${environment.apiUrl}artistes/search/${search}`, {observe: 'response'});
    }
}
