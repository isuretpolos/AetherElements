import { Component, OnInit } from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-new-case',
  templateUrl: './new-case.component.html',
  styleUrls: ['./new-case.component.scss']
})
export class NewCaseComponent implements OnInit {

  name = new FormControl('');
  description = new FormControl('');

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {
  }

}
