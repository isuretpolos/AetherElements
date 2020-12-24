import { Injectable } from '@angular/core';
import {User} from "../domains/User";
import {Observable} from "rxjs";
import {Case} from "../domains/Case";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CaseService {

  public static url = `${environment.serverUrl}/case/`;

  public user:User | null = null;

  constructor(
    private http:HttpClient
  ) { }

  public getAllCases():Observable<Case[]> {

    console.log(this.user);

    let headers = new HttpHeaders();

    // @ts-ignore
    headers = headers.set('userName', this.user.username);
    // @ts-ignore
    headers = headers.set('password', this.user.password);

    return this.http.get<Case[]>(`${CaseService.url}`, {headers:headers});
  }
}
