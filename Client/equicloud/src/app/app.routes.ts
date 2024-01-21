import { RouterModule, Routes } from '@angular/router';
import { ListFilesComponent } from './list-files/list-files.component';
import { LoginComponent } from './login/login.component';
import { NgModule } from '@angular/core';
import { RegisterComponent } from './register/register.component';

export const routes: Routes = [
    {path:"files", component: ListFilesComponent},
    {path: "login", component: LoginComponent},
    {path : "register", component: RegisterComponent}
];
@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
  })

  export class AppRoutingModule {}