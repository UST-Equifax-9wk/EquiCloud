import { Component } from '@angular/core';
import { RemoteService, Upload } from '../remote.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
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
  folders:Array<Folder>=[]
  visible:Array<boolean>=[];
  search="";
  lastSearch="";
  sortBy="file name";
  ascending=true;
  remote:RemoteService;
  constructor(remote:RemoteService){
    this.remote=remote;
    this.remote.getAllFiles().subscribe({
      next:(data)=>{
        this.populate(data,false)        
      },
      error:(error:HttpErrorResponse)=>{
        alert("Error retrieving files");
        console.log(error);
      }
    }) 
  }


  download(file:Upload){
    alert("download not implemented.");
    console.log(file);
  }


  show(folder:Folder){
    folder.visible = !folder.visible;
  }


  populate(data:HttpResponse<Object>, vis:boolean){
    for(let upload of data.body as Array<Upload>){
      let path=upload.path.split('\\')
      let push=true;
      let parentFolder:Folder={
          folder: "",
          nested: [],
          files: [],
          visible:false
        };
      for(let folder of this.folders){
        if(folder.folder==path[0]){
          parentFolder=folder;
          push=false;
        }
      }
      if(push){
        let newFolder:Folder={
          folder: path[0],
          nested: [],
          files: [],
          visible:false
        };
        this.folders.push(newFolder)
        parentFolder=newFolder;
      }

      // let navFolder:Folder={
      //   folder: "",
      //   nested: [],
      //   files: []
      // };
      for(let i=1;i<path.length;i++){
        let noParent=true;        
        if(i!=path.length-1){
          for(let folder of parentFolder.nested){
            if(folder.folder==path[i]){
              parentFolder=folder;
              noParent=false;
            }
          }
          if(noParent){

            let newFolder:Folder={
              folder: path[i],
              nested: [],
              files: [],
              visible:false
            };
            parentFolder.nested.push(newFolder);
            parentFolder=newFolder;
          }
        }
        else{
          parentFolder.files.push(upload)
        }
      }

      // for(let folder of this.folders){
      //   if(folder.folder==path[0]){
      //     push=false;
      //     folder.files.push(upload)
      //   }
      // }
      // if(push){
      //   let newFolder:Folder={
      //     folder: path[0],
      //     nested: [],
      //     files: []
      //   };
      //   newFolder.files.push(upload)
      //   this.folders.push(newFolder)
      //   this.visible.push(vis);
      // }     
    }
    console.log("folders:", this.folders)
    this.sort();
  }


  refresh(){
    this.folders=[];
    this.visible=[];
    if(this.search==this.lastSearch){
      this.sort();
      
    }
    if(this.search==""){
      this.remote.getAllFiles().subscribe({
        next:(data)=>{
          this.populate(data,false);
        },
        error:(error:HttpErrorResponse)=>{
          alert("Error retrieving files");
          console.log(error);
        }
      })
    }
    else{
      this.remote.getFilesContaining(this.search).subscribe({
        next:(data)=>{
          this.populate(data,true);
        },
        error:(error:HttpErrorResponse)=>{
          alert("Error retrieving files");
          console.log(error);
        }
      })
    }
  }
  

  sort(){
    if(this.sortBy=="file name"){
      if(this.ascending){
        this.folders.sort((a,b)=>a.folder.localeCompare(b.folder))
      }
      if(!this.ascending){
        this.folders.sort((a,b)=>b.folder.localeCompare(a.folder))
      }
    }
  }
}

export interface Folder{
  folder:string;
  nested:Array<Folder>;
  files:Array<Upload>;
  visible:boolean;
}

