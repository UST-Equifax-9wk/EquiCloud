import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {
  private username : string = "DEFAULT FROM SERVICE"

  setUsername(username : string): void {
    this.username = username
  } //DEPRICATED

  getUsername() : string {
    return this.username
  } //DEPRICATED

  getLoggedInUsername(): string | null {
    const user = sessionStorage.getItem('auth-user');
    if (user) {
      const userData = JSON.parse(user);
      return userData.username;
    }
    return null;
  }

  constructor() { }
}
