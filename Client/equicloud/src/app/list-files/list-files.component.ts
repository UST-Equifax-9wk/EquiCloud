import { Component } from '@angular/core';
import { RemoteService, Upload } from '../remote.service';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-list-files',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './list-files.component.html',
  styleUrl: './list-files.component.css'
})
export class ListFilesComponent {
  files:Array<Upload>=[];

  constructor(remote:RemoteService){
    remote.getAllFiles().subscribe({
      next:(data)=>{
        this.files=data.body as Array<Upload>
      },
      error:(error:HttpErrorResponse)=>{
        alert("Error retrieving files");
        console.log(error);
      }
    })
  }
}
