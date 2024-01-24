import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RemoteService } from './remote.service';

@Injectable({
  providedIn: 'root'
})
export class DownloadfileService {

  baseUrl = "http://localhost:8080";

  constructor(private http:HttpClient, remote:RemoteService) {
    this.http = http;
    this.baseUrl = remote.baseUrl;
  }

  downloadFile(fileName:string) {
    return this.http.get(this.baseUrl + "/download",
     {
      observe: 'response',
      withCredentials: true, 
      responseType: 'blob',
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      params: new HttpParams().set('filePath', fileName)
    })
  }
}
