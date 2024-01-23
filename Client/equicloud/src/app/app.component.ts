import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterOutlet } from '@angular/router';
import { ListFilesComponent } from './list-files/list-files.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

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
  title = 'revportal';
  loggedIn = localStorage.getItem("jwtToken")!=null;
  logout(){
      localStorage.removeItem('jwtToken');
      window.location.replace("login")
      }
  
  navigateToUpload() {
    window.location.replace("file-upload")
  }
}
