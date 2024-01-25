import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterOutlet } from '@angular/router';
import { ListFilesComponent } from './list-files/list-files.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { RemoteService } from './remote.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule
    ,ListFilesComponent
    , LoginComponent
    , RegisterComponent
    , FileUploadComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'equicloud';
  loggedIn = sessionStorage.getItem("auth-user")!=null;
  constructor (private remoteService: RemoteService){}
  logout(){
    this.remoteService.logout().subscribe(
      response => {
        sessionStorage.removeItem("auth-user");
        window.location.replace("");
      },
      error => {
        console.log("Logout Error");
      }
    );
      }
  
  navigateToUpload() {
    window.location.replace("file-upload")
  }

  navigateToFileList() {
    window.location.replace("files")
  }
}
