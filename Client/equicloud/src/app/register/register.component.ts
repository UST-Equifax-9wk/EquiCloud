import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AccountDto, RemoteService } from '../remote.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  firstName : string = ''
  lastName : string = ''
  accountName : string = ''
  password : string = ''
  email : string = ''

  constructor (private remoteService: RemoteService) {}

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
        console.log("Registration Success");
        window.location.replace("login")
      },
      error => {
        console.log("Registraion failed")
      }
    );
  }
}
