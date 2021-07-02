import {Component, OnInit} from '@angular/core';
import {CaseService} from "../../services/case.service";
import {Case} from "../../domains/Case";
import {Router} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  cases:Case[] = [];

  constructor(
    private caseService:CaseService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.caseService.getAllCases().subscribe( c => {
      this.cases = c;
    })
  }

  navigateToCase(uuid: string) {
    console.log(uuid);
    this.router.navigate(['case',uuid]);
  }
}
