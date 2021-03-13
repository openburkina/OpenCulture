import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import {environment} from '../../../../environments/environment';
import {User} from '../model/user-connect.model';

@Injectable({providedIn: 'root'})
export class AuthenticateService {
  private currentUserSubject: BehaviorSubject<User>;
  private currentUser: Observable<User>;

  constructor(private http: HttpClient ){
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public currentUserValue(): User{
    return this.currentUserSubject.value;
  }

  login(username: string, password: string ): Observable<HttpClient>{
    return this.http.post<any>(`${environment.apiUrl}/authenticate`, {username, password})
    .pipe(map(user => {
      localStorage.setItem('currentUser', JSON.stringify(user));
      this.currentUserSubject.next(user);
      return user;
    }));
  }

  logout(): void{
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}
