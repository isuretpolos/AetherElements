import { Component, OnInit } from '@angular/core';
import {CaseService} from "../../services/case.service";
import {Case} from "../../domains/Case";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-edit-case',
  templateUrl: './edit-case.component.html',
  styleUrls: ['./edit-case.component.scss']
})
export class EditCaseComponent implements OnInit {

  uuid: string | null = "";
  case:Case  |null = null;

  constructor(private caseService:CaseService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.uuid = this.route.snapshot.paramMap.get('uuid');
  }

}
