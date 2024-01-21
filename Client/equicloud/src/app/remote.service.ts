import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RemoteService {
  httpClient : HttpClient
  baseUrl:string;
  constructor(client: HttpClient) {
    this.httpClient = client
    this.baseUrl="http://localhost:8080"
   }
   getAllFiles(){
    return this.httpClient.get(this.baseUrl+"/files",
    {
      observe: 'response',
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type' : 'application/json'
      })
    }
    )
  }


  login(accountName: string, password: string) {
    return this.httpClient.post(this.baseUrl+"/auth/login", {accountName,password},
    {
      observe: 'response',
      headers: new HttpHeaders({
        'Content-Type' : 'application/json'
      })
    }
    )
  }

  register(account : AccountDto) {
    return this.httpClient.post(this.baseUrl + "/auth/register", account)
  }

  test() {
    return this.httpClient.get<string>(this.baseUrl+"/test",
    { responseType: 'text' as 'json' }
    )
  }
  
}


export interface AccountDto {
  accountName : String
  firstName : String
  lastName : String
  password : String
  email : String
}

export interface Upload {
  fileName : String
  fileDescription : String
  filePath : String
  timeCreated?: String
}