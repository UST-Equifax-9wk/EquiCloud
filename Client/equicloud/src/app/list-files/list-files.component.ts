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
  sortBy="Alphabetical";
  ascending=true;
  remote:RemoteService;

  constructor(remote:RemoteService){      
    this.remote=remote;
    if(localStorage.getItem("jwtToken")==null){
      window.location.replace("login")
    }
    else{
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
          visible:false,
          uploadDate:""
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
          visible:false,
          uploadDate:""
        };
        this.folders.push(newFolder)
        parentFolder=newFolder;
      }
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
              visible:false,
              uploadDate:""
            };
            parentFolder.nested.push(newFolder);
            parentFolder=newFolder;
          }
        }
        else{
          parentFolder.files.push(upload)
        }
      }   
    }
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
    if(this.sortBy=="Alphabetical"){
      if(this.ascending){
        this.folders.sort((a,b)=>a.folder.localeCompare(b.folder))
        for(let folder of this.folders){
          folder.files.sort((a,b)=>a.fileName.localeCompare(b.fileName))
          folder.nested.sort((a,b)=>a.folder.localeCompare(b.folder))
          for(let nested of folder.nested){
            nested.files.sort((a,b)=>a.fileName.localeCompare(b.fileName))
          }
        }
      }
      if(!this.ascending){
        this.folders.sort((a,b)=>b.folder.localeCompare(a.folder))
        for(let folder of this.folders){
          folder.files.sort((a,b)=>b.fileName.localeCompare(a.fileName))
          folder.nested.sort((a,b)=>b.folder.localeCompare(a.folder))
          for(let nested of folder.nested){
            nested.files.sort((a,b)=>b.fileName.localeCompare(a.fileName))
          }
        }
      }
    }
    else{
      if(this.ascending){
        for(let folder of this.folders){
          for(let nested of folder.nested){
            nested.files.sort((a,b)=>a.uploadDate<b.uploadDate ? -1: a.uploadDate>b.uploadDate ? 1:0)
            nested.uploadDate=nested.files[0].uploadDate;
          }
          folder.files.sort((a,b)=>a.uploadDate<b.uploadDate ? -1: a.uploadDate>b.uploadDate ? 1:0)
          folder.nested.sort((a,b)=>a.uploadDate<b.uploadDate? -1: a.uploadDate>b.uploadDate ? 1:0)
          
          if(folder.files.length>0){
            if(folder.nested.length>0){
              if(folder.files[0].uploadDate>folder.nested[0].uploadDate)folder.uploadDate=folder.nested[0].uploadDate
              else folder.uploadDate=folder.files[0].uploadDate;
            }
            else folder.uploadDate=folder.files[0].uploadDate
          }
          else folder.uploadDate=folder.nested[0].uploadDate;        }
      
        this.folders.sort((a,b)=>a.uploadDate<b.uploadDate? -1: a.uploadDate>b.uploadDate ? 1:0)
      }
      if(!this.ascending){
        for(let folder of this.folders){
          for(let nested of folder.nested){
            nested.files.sort((a,b)=>a.uploadDate<b.uploadDate ? 1: a.uploadDate>b.uploadDate ? -1:0)
            nested.uploadDate=nested.files[0].uploadDate;
          }
          folder.files.sort((a,b)=>a.uploadDate<b.uploadDate ? 1: a.uploadDate>b.uploadDate ? -1:0)
          folder.nested.sort((a,b)=>a.uploadDate<b.uploadDate? 1: a.uploadDate>b.uploadDate ? -1:0)
          

          if(folder.files.length>0){
            if(folder.nested.length>0){
              if(folder.files[0].uploadDate<folder.nested[0].uploadDate)folder.uploadDate=folder.nested[0].uploadDate
              else folder.uploadDate=folder.files[0].uploadDate;
            }
            else folder.uploadDate=folder.files[0].uploadDate
          }
          else folder.uploadDate=folder.nested[0].uploadDate;

          
        }
        this.folders.sort((a,b)=>a.uploadDate<b.uploadDate? 1: a.uploadDate>b.uploadDate ? -1:0)
      }
    }
  }
  
}

export interface Folder{
  folder:string;
  nested:Array<Folder>;
  files:Array<Upload>;
  visible:boolean;
  uploadDate:string;
}

