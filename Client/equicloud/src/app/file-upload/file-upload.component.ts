import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RemoteService, Upload } from '../remote.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.css'
})
export class FileUploadComponent {
  onFileSelected(event: any): void {
    const file: File = event.target.files[0]
    if(file) {
      this.file = file;
      console.log('Selected file:', file)
    }
  }

  router : Router
  remoteService : RemoteService
  fileName : string = ""
  fileDescription : string = ""
  filePath : string = ""
  file : any
  currentUsername : string = ""
  data: any[] = []

  constructor(router : Router, remoteService : RemoteService) {
    this.router = router
    this.remoteService = remoteService
  }

  upload() {
    const formData = new FormData();
    console.log("file contents", this.file)
    formData.append('file', this.file)
    let upload : Upload = {
      fileName : this.fileName,
      description : this.fileDescription,
      path : this.filePath,
    }
    this.remoteService.uploadFile(formData).subscribe({
      next: (data) => {
        alert(`${this.fileName} has successfully been uploaded.`)
        console.log(data)
      },
      error: (error:HttpErrorResponse) => {
        alert("Failed to upload file.")
        console.log(error.error)
      }
    })

    this.remoteService.uploadMetadata(upload).subscribe({
      next: (data) => {
        console.log("Sucessfully uploaded file metadata", data)
      },
      error: (error:HttpErrorResponse) => {
        console.log("Error uploading file metadata", error.error)
      }
    })
  }
}
