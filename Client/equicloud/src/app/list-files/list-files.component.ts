import { Component } from '@angular/core';
import { RemoteService, Upload } from '../remote.service';
import { HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-list-files',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './list-files.component.html',
  styleUrl: './list-files.component.css'
})
export class ListFilesComponent {
  files:Array<Array<Upload>>=[];
  folders:Array<string>=[];
  visible:Array<boolean>=[];
  search="";
  sortBy="";
  ascending=true;
  remote:RemoteService;
  constructor(remote:RemoteService){
    this.remote=remote;
    this.populate();
  }

  download(file:Upload){
    alert("download not implemented.");
    console.log(file);
  }

  show(index:number){
    this.visible[index] = !this.visible[index]
  }
  populate(){
    this.remote.getAllFiles().subscribe({
      next:(data)=>{
        // this.files=data.body as Array<Upload>

        for(let upload of data.body as Array<Upload>){
          let path=upload.path.split('\\')
          if(this.folders.includes(path[0])){
            this.files[this.folders.indexOf(path[0])].push(upload)
          }
          else{
            this.folders.push(path[0]);
            let toAdd:Array<Upload>=[];
            toAdd.push(upload);
            this.files.push(toAdd);
            this.visible.push(false);
          }
        }
      },
      error:(error:HttpErrorResponse)=>{
        alert("Error retrieving files");
        console.log(error);
      }
    })
  }
  refresh(){
    console.log("search "+this.search +" sortBy "+this.sortBy+" ascending "+this.ascending)
  }
}
