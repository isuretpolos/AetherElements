import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CaseService} from "../services/case.service";
import {CookieService} from "ngx-cookie-service";
import {User} from "../domains/User";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {NewCaseComponent} from "./new-case/new-case.component";
import {Case} from "../domains/Case";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  title = 'ui';
  user: User | undefined;

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
      this.user = new User();
      this.user.username = this.cookieService.get('userName');
      this.user.password = this.cookieService.get('password');
      this.caseService.user = this.user;
    }

    if (this.caseService.user == null) {
      this.navigateToLoginPage();
    }
  }

  private navigateToLoginPage() {
    this.router.navigate(['login'], {relativeTo: this.route});
  }

  switchAccount() {
    this.caseService.user = undefined;
    return false;
  }

  addNewCase() {
    const modalRef = this.modalService.open(NewCaseComponent);

    modalRef.closed.subscribe( e => {
      if (e != 'Close click') return;

      let caseObject:Case = new Case();
      caseObject.name = modalRef.componentInstance.name.value;
      caseObject.description = modalRef.componentInstance.description.value;
      this.caseService.saveCase(caseObject).subscribe(result => console.log('done'));
    });
    return false;
  }
}
