import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, RouterOutlet } from '@angular/router';
import { ListFilesComponent } from './list-files/list-files.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { RemoteService } from './remote.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterModule
    ,ListFilesComponent
    , LoginComponent
    , RegisterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'revportal';
  loggedIn = sessionStorage.getItem("auth-user")!=null;
  constructor (private remoteService: RemoteService){}
  logout(){
    this.remoteService.logout().subscribe(
      response => {
        console.log("Logged out successfully");
        sessionStorage.removeItem("auth-user");
        window.location.replace("login");
      },
      error => {
        console.log("Logout Error");
      }
    );
      }
}
