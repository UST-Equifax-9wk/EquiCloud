import { Routes } from '@angular/router';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { ListFilesComponent } from './list-files/list-files.component';

export const routes: Routes = [
    {path: "file-upload", component: FileUploadComponent},
    {path: "files", component: ListFilesComponent}
];
