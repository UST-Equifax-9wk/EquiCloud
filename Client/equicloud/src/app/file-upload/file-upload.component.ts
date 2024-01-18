import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RemoteService, Upload } from '../remote.service';

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
  file ?: File

  constructor(router : Router, remoteService : RemoteService) {
    this.router = router
    this.remoteService = remoteService
  }

  upload() {
    let upload : Upload = {
      fileName : this.fileName,
      description : this.fileDescription,
      path : this.filePath,
      file : this.file
    }
    console.log(upload)
  }
}
