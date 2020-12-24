import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CaseService} from "./services/case.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'ui';

  constructor(
    private router:Router,
    private route: ActivatedRoute,
    private caseService:CaseService
  ) { }

  ngOnInit(): void {
    console.log('init AppComponent');
    if (this.caseService.user == null) {
      //this.navigateToLoginPage();
    }
  }

  private navigateToLoginPage() {
    this.router.navigate(['login'], {relativeTo: this.route});
  }

  switchAccount() {
    this.caseService.user = null;
    this.navigateToLoginPage();
    return false;
  }
}
