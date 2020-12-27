import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CaseService} from "../services/case.service";
import {CookieService} from "ngx-cookie-service";
import {User} from "../domains/User";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

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
    private caseService:CaseService,
    private cookieService:CookieService,
    private modalService: NgbModal
  ) { }

  ngOnInit(): void {
    console.log('init AppComponent');

    if (this.cookieService.get('userName') != null) {
      let user:User = new User();
      user.username = this.cookieService.get('userName');
      user.password = this.cookieService.get('password');
      this.caseService.user = user;
    }

    if (this.caseService.user == null) {
      this.navigateToLoginPage();
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
