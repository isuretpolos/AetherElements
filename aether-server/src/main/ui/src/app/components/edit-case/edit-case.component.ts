import { Component, OnInit } from '@angular/core';
import {CaseService} from "../../services/case.service";
import {Case, Paragraph, Session} from "../../domains/Case";
import {ActivatedRoute} from "@angular/router";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-edit-case',
  templateUrl: './edit-case.component.html',
  styleUrls: ['./edit-case.component.scss']
})
export class EditCaseComponent implements OnInit {

  uuid: string | null = "";
  case:Case | null = null;
  newSession:Session | null = null;
  intention = new FormControl('');
  description = new FormControl('');
  note = new FormControl('');
  editParagraph:Paragraph | null = null;

  constructor(private caseService:CaseService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.uuid = this.route.snapshot.paramMap.get('uuid');
    this.caseService.case.subscribe( c => this.case = c );
    this.caseService.getCase(this.uuid).subscribe( c => {
      this.caseService.changeCase(c);
    });
  }

  addSession() {
    this.newSession = new Session();
  }

  saveCase() {

    if (this.newSession != null && this.case != null) {

      if (this.editParagraph != null) {
        this.editParagraph.note = this.note.value;
        this.newSession.paragraphs.push(this.editParagraph);
        console.log(this.newSession)
      }

      this.newSession.created = new Date();
      this.newSession.intention = this.intention.value;
      this.newSession.description = this.description.value;
      this.case.sessionList.push(this.newSession);
    }

    this.caseService.saveCase(this.case).subscribe( c => {
      this.newSession = null;
      this.editParagraph = null;
    });
  }

  addParagraph() {
    let par = new Paragraph();
    this.editParagraph = par;
  }

  cancelNewSession() {
    this.newSession = null;
  }

  cancelNewParagraph() {
    this.editParagraph = null;
  }
}
