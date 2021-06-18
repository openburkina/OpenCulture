import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {User} from '../../models/User';
import {environment} from '../../../../environments/environment';
import {Observable} from 'rxjs';
import {LoginVM} from "../../models/login-vm";
import {Abonnement} from "../../models/abonnement";
import {Password} from "../../models/Password";

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

    doChangePassword(password: Password): Observable<HttpResponse<User>> {
        return this.http.post<User>(`${environment.apiUrl}account/change-password`, password, {observe: 'response'});
    }

    doAbonnement(abonnement: Abonnement): Observable<HttpResponse<Abonnement>> {
        return this.http.post<Abonnement>(`${environment.apiUrl}abonnements`, abonnement, {observe: 'response'});
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

    onSearch(search: String, typeFile?: string): Observable<HttpResponse<any>> {
        return this.http.get(`${environment.apiUrl}artiste-oeuvres/search/${search}/${typeFile}`, {observe: 'response'});
    }

    getAbonnement(id?: number): Observable<HttpResponse<any>> {
        return this.http.get(`${environment.apiUrl}abonnements/user-id/${id}`, {observe: 'response'});
    }

    sendEmailPaiment(): Observable<HttpResponse<User>> {
        return this.http.get<LoginVM>(`${environment.apiUrl}abonnements/send-email`, {observe: 'response'});
    }

}
