import { Component, OnInit } from '@angular/core';
import {CaseService} from "../services/case.service";
import {Case} from "../domains/Case";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  constructor(
    private caseService:CaseService
  ) { }

  ngOnInit(): void {
    this.caseService.getAllCases().subscribe( c => {
      console.log(c)
    })
  }

}
