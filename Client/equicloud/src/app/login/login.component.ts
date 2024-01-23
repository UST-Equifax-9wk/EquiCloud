import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RemoteService, AuthResponse } from '../remote.service';
import { Router, RouterLink, RouterModule } from '@angular/router';
import { CurrentUserService } from '../current-user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule,CommonModule, RouterLink, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent { 

  accountName: string = '';
  password: string = '';

  loginMessage: string = '';
  constructor (private remoteService: RemoteService,
    private router:Router, private currentUserService : CurrentUserService) {}

  login() {
    console.log("accountName: " + this.accountName)
    console.log("password: " + this.password)
    this.remoteService.login(this.accountName, this.password).subscribe(
      (response: AuthResponse) => {
        sessionStorage.setItem('auth-user', JSON.stringify(response));
        console.log("Authentication Success");
        this.currentUserService.setUsername(this.accountName)
        console.log("current user BEFORE navigation: " + this.currentUserService.getUsername())
        window.location.replace("files")
        //this.router.navigate([`/files`])
        console.log("current user AFTER navigation: " + this.currentUserService.getUsername())
      },
      error => {
        this.loginMessage = 'Invalid username or password.';
      }
    );
  } 

    
    
}
