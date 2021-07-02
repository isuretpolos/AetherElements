import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {LoginComponent} from "./components/login/login.component";
import {EditCaseComponent} from "./components/edit-case/edit-case.component";

const routes: Routes = [
  { path: 'home', component: DashboardComponent },
  { path: 'login', component: LoginComponent },
  { path: 'case/:uuid', component: EditCaseComponent },
  { path: '**', redirectTo: 'home', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
