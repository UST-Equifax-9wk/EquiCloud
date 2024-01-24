import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AccountDto, RemoteService } from '../remote.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule,RouterLink , CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  firstName : string = ''
  lastName : string = ''
  accountName : string = ''
  password : string = ''
  email : string = ''
  message="";
  constructor (private remoteService: RemoteService,
    private router: Router) {
      if(sessionStorage.getItem("auth-user")!=null){
        window.location.replace("files")
      }
    }

  register(){
    let account : AccountDto = {
      firstName : this.firstName,
      lastName : this.lastName,
      accountName : this.accountName,
      password : this.password,
      email : this.email,
    }
    this.remoteService.register(account).subscribe(
      response => {
<<<<<<< HEAD
        console.log("Registration Success");
        window.location.replace("");
=======
        alert("Registration Success");
        window.location.replace("")
>>>>>>> a55d536c3082cec89383ba22d2909d50d70f0386
      },
      error => {
        this.message="Duplicate username or email"
      }
    );
  }
}
