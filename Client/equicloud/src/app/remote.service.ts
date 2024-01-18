import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

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

   uploadMetadata(upload : Upload) { //Maybe add in a username request param once that gets implemented in the controller
    return this.httpClient.post(this.baseUrl+`/upload`, JSON.stringify(upload),
    {
      observe: 'response',
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type' : 'application/json'
      })
    })
   }

  //  uploadFile(file : File) {
  //   return this.httpClient.post(this.baseUrl+`/upload`, file,
  //   {
  //     observe: 'response',
  //     withCredentials: true,
  //     headers: new HttpHeaders({
  //       'Content-Type' : 'application/json'
  //     })
  //   })
  //  }
}


export interface Upload {
  fileName : string
  description : string
  path : string
  uploadDate?: string
  file?: File
}