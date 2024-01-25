import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RemoteService {
  httpClient : HttpClient
  baseUrl : string

  private registrationMessageSource = new BehaviorSubject<string>('');
  currentMessage = this.registrationMessageSource.asObservable();

  constructor(client: HttpClient) {
    this.httpClient = client
    //hosted url
    this.baseUrl="http://34.125.212.192:7777"
    //local url
    //this.baseUrl = "http://localhost:8080"
   }

   uploadFile(formData : any) : Observable<any> { 
    return this.httpClient.post(this.baseUrl+`/upload`, formData,
      {
        responseType : 'text'
      }
    )
  }

   uploadMetadata(upload : Upload) {
    return this.httpClient.post(this.baseUrl+`/upload-metadata`, JSON.stringify(upload),
    {
      observe: 'response',
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type' : 'application/json'
      })
    })
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

  getFilesContaining(containing:string){
    return this.httpClient.get(this.baseUrl+"/files/"+containing,
    {
      observe: 'response',
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type' : 'application/json'
      })
    })
  }

  login(accountName: string, password: string) : Observable<AuthResponse>  {
    return this.httpClient.post<AuthResponse>(this.baseUrl+"/auth/login",
     {accountName,password},
    {withCredentials: true})
  }

  register(account : AccountDto) {
    return this.httpClient.post(this.baseUrl + "/auth/register", account)
  }

  test() {
    return this.httpClient.get<string>(this.baseUrl+"/test",
    { responseType: 'text' as 'json', withCredentials:true }
    )
  }

  logout(){
    return this.httpClient.get(this.baseUrl+"/auth/logout",{
      observe: 'response',
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type' : 'application/json'
      })
    }
    )
  }
  
  changeMessage(message: string) {
    this.registrationMessageSource.next(message);
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
  fileName : string
  description : string
  path : string
  uploadDate: string
}

export interface AuthResponse {
  username : String
  email : String
  firstName : String
  lastName : String
}
