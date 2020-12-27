export class Case {
  uuid:string = '';
  name:string = '';
  description:string = '';
  created:Date = new Date();
  lastChange:Date = new Date();
  sessionList:Session[] = [];
  topTenList:Rate[] = [];
}

export class Session {
  uuid:string = '';
  created:Date = new Date();
  intention:string = '';
  description:string = '';
  paragraphs:Paragraph[] = [];
}

export class Paragraph {
  uuid:string = '';
  note:Note | null = null;
  analysisResult:AnalysisResult | null = null;
  broadCastData:BroadCastData | null = null;
}

export class BroadCastData {
  uuid:string = '';
  created:Date = new Date();
  clear:boolean = false;
  intention:string = '';
  signature:string = '';
  delay:number = 25;
  repeat:number = 1;
  enteringWithGeneralVitality:number = 0;
  leavingWithGeneralVitality:number = 0;
}

export class AnalysisResult {
  uuid:string = '';
  created:Date = new Date();
  generalVitality:number = 0;
  rateList:Rate[] = [];
}

export class Note {
  uuid:string = '';
  created:Date = new Date();
  text:string = '';
}

export class Rate {
  uuid:string = '';
  energeticValue:number = 0;
  name:string = '';
  url:string = '';
  gv:string = '';
  recurring:number = 0;
  recurringGeneralVitality:number = 0;
}
