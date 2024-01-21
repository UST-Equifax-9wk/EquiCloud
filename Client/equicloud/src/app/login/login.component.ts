import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RemoteService } from '../remote.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule,CommonModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent { 

  accountName: string = '';
  password: string = '';

  constructor (private remoteService: RemoteService,
    private router:Router) {}

  login() {
    console.log("accountName: " + this.accountName)
    console.log("password: " + this.password)
    this.remoteService.login(this.accountName, this.password).subscribe(
      (response: any) => {
        console.log("Response:", response);
          localStorage.setItem('jwtToken', response.body.token);
          console.log("token: " + response.body.token)
        console.log("Authentication Success");
      },
      error => {
        console.log("Authentication failed")
      }
    );
  } 

    test() {

      this.remoteService.test().subscribe(
        response => {
          console.log(response)
        },
        error => {
          console.log("test failed" + error)
        }
      )
    }
    logout(){
      localStorage.removeItem('jwtToken');
      this.router.navigate(['/login']);
      }
}
