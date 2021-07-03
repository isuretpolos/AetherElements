import { Injectable } from '@angular/core';
import {User} from "../domains/User";
import {Observable} from "rxjs";
import {Case} from "../domains/Case";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CaseService {

  public static url = `${environment.serverUrl}/case/`;

  public user: User | undefined;

  constructor(
    private http:HttpClient
  ) { }

  getAllCases():Observable<Case[]> {

    if (this.user == null) {
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

  private getHeader():HttpHeaders {

    let headers:HttpHeaders = new HttpHeaders();
    // @ts-ignore
    headers = headers.set('userName', this.user.username);
    // @ts-ignore
    headers = headers.set('password', this.user.password);

    return headers;
  }
}
