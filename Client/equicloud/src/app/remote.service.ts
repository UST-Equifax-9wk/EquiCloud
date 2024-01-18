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
}


export interface Upload {
  fileName : String
  fileDescription : String
  filePath : String
  timeCreated?: String
}