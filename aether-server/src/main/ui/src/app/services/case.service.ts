import { Injectable } from '@angular/core';
import {User} from "../domains/User";
import {BehaviorSubject, Observable} from "rxjs";
import {Case} from "../domains/Case";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CaseService {

  public static url = `${environment.serverUrl}/case/`;

  // observables
  private caseSource = new BehaviorSubject<Case|null>(null);
  private useSource = new BehaviorSubject<User|null>(null);
  user = this.useSource.asObservable();
  case = this.caseSource.asObservable();
  userObject: User | null | undefined;

  constructor(
    private http:HttpClient
  ) {
    this.user.subscribe(u => this.userObject = u);
  }

  getAllCases():Observable<Case[]> {
console.log(this.userObject)
    if (this.userObject == null) {
      return new Observable<Case[]>();
    }

    return this.http.get<Case[]>(`${CaseService.url}`, {headers:this.getHeader()});
  }

  getCase(id: string | null):Observable<Case> {

    if (this.user == null) {
      return new Observable<Case>();
    }
    console.log(`${CaseService.url}${id}`);
    return this.http.get<Case>(`${CaseService.url}${id}`, {headers:this.getHeader()});
  }

  saveCase(caseObject: Case | null):Observable<void> {
    return this.http.post<void>(`${CaseService.url}`, caseObject, {headers:this.getHeader()});
  }

  changeCase(caseObject: Case) {
    this.caseSource.next(caseObject);
  }

  changeUser(user: User) {
    this.useSource.next(user);
  }

  private getHeader():HttpHeaders {

    let headers:HttpHeaders = new HttpHeaders();
    // @ts-ignore
    headers = headers.set('userName', this.userObject.username);
    // @ts-ignore
    headers = headers.set('password', this.userObject.password);

    return headers;
  }
}
