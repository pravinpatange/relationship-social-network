import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = '/api/users';

  constructor(private http: HttpClient) {}

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/me`);
  }

  updateProfile(data: any): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/me`, data);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }
}
