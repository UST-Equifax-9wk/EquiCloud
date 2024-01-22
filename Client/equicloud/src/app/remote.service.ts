import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RemoteService {
  httpClient : HttpClient
  baseUrl : String

  constructor(client: HttpClient) {
    this.httpClient = client
    this.baseUrl = "http://localhost:8080"
   }

   uploadFile(formData : any) : Observable<any> { //Maybe add in a username request param once that gets implemented in the controller
    return this.httpClient.post(this.baseUrl+`/upload`, formData,
      {
        responseType : 'text'
      }
    )
  }

   uploadMetadata(upload : Upload) {
    return this.httpClient.post(this.baseUrl+`/uploadMetadata`, JSON.stringify(upload),
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
}


export interface Upload {
  fileName : string
  description : string
  path : string
  uploadDate?: string
}
