import { RouterModule, Routes } from '@angular/router';
import { ListFilesComponent } from './list-files/list-files.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { LoginComponent } from './login/login.component';
import { NgModule } from '@angular/core';
import { RegisterComponent } from './register/register.component';

export const routes: Routes = [
    {path:"files", component: ListFilesComponent},
    {path: "file-upload", component: FileUploadComponent},
    {path: "", component: LoginComponent},
    {path : "register", component: RegisterComponent}
];
@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
  })

  export class AppRoutingModule {}