import {Component, OnInit} from '@angular/core';
import {FormControl} from "@angular/forms";
import {CaseService} from "../services/case.service";
import {User} from "../domains/User";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  username = new FormControl('');
  password = new FormControl('');

  constructor(
    private caseService: CaseService,
    private router:Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
  }

  login() {
    let user:User = new User();
    user.username = this.username.value;
    user.password = this.password.value;

    this.caseService.user = user;

    this.caseService.getAllCases().subscribe(c => {
      console.log(c);
      this.navigateToLoginPage();
    })
  }

  private navigateToLoginPage() {
    this.router.navigate(['home'], {relativeTo: this.route});
  }
}
