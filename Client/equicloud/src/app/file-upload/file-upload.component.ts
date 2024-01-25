import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RemoteService, Upload } from '../remote.service';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { CurrentUserService } from '../current-user.service';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.css'
})
export class FileUploadComponent implements OnInit{
  onFileSelected(event: any): void {
    const file: File = event.target.files[0]
    if(file) {
      this.file = file;
      this.actualFileName = file.name
      const indexOfPeriod = this.actualFileName.indexOf('.')
      this.fileExtension = indexOfPeriod !== -1 ? this.actualFileName.substring(indexOfPeriod+1) : this.actualFileName
    }
  }
  httpClient = inject(HttpClient)
  router : Router
  remoteService : RemoteService
  currentUserService : CurrentUserService
  fileName : string = ""
  actualFileName : string = ""
  fileExtension : string = ""
  fileDescription : string = ""
  filePath : string = ""
  file : any
  currentUsername : string | null = "DEFAULT"
  data: any[] = []
  newFolder : boolean = false
  newFolderName : string = ""

  constructor(router : Router, remoteService : RemoteService, currentUserService : CurrentUserService) {
    this.router = router
    this.remoteService = remoteService
    this.currentUserService = currentUserService
    if(sessionStorage.getItem("auth-user") == null) {
      window.location.replace("")
    } else {
      this.currentUsername = currentUserService.getLoggedInUsername()
    }
  }

  ngOnInit(): void {
      this.fetch()
  }

  fetch() {
    this.httpClient.get(`${this.remoteService.baseUrl}/${this.currentUsername}/folders`)
    .subscribe((data: any) => {
      this.data = data;
    });
  }

  upload() {
    if(this.fileName == "") {
      alert("Please provide a file name.")
      return
    }
    this.fileName = `${this.fileName}.${this.fileExtension}`
    if(this.newFolder) {
      this.filePath = `/${this.newFolderName}/${this.fileName}`
    } else {
      if(this.filePath == "") {
        this.filePath = `/${this.fileName}`
      } else {
        this.filePath = `/${this.filePath}/${this.fileName}`
      }
    }

    let upload : Upload = {
      fileName : this.fileName,
      description : this.fileDescription,
      path : `${this.currentUsername}${this.filePath}`,
      uploadDate : ""
    }

    this.currentUsername = this.currentUserService.getLoggedInUsername()
    const formData = new FormData();
    formData.append('file', this.file)
    formData.append('fileName', upload.path)
    formData.append('description', this.fileDescription)
    
    this.remoteService.uploadFile(formData).subscribe({
      next: (data) => {
        alert(`${this.fileName} has successfully been uploaded.`)
        window.location.replace("file-upload")
      },
      error: (error:HttpErrorResponse) => {
        alert("Failed to upload file.")
        console.log(error.error)
      }
    })

    this.filePath = ""
  }
}
