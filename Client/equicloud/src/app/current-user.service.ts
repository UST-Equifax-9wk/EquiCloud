import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {
  private username : string = "DEFAULT FROM SERVICE"

  setUsername(username : string): void {
    this.username = username
  }

  getUsername() : string {
    return this.username
  }

  constructor() { }
}
