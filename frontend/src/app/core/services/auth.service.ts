import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface User {
  id: number;
  username: string;
  email: string;
  displayName: string;
  profilePictureUrl?: string;
  bio?: string;
  location?: string;
  website?: string;
  accountType?: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken?: string;
  userId?: number;
  username?: string;
  email?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api/auth';
  private readonly TOKEN_KEY = 'rsn_token';
  
  public currentUser = signal<User | null>(null);

  constructor(private http: HttpClient) {
    this.checkToken();
  }

  register(data: any): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.apiUrl}/register`, data);
  }

  login(data: any): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.apiUrl}/login`, data).pipe(
      tap(response => {
        if (response.success && response.data?.accessToken) {
          this.setToken(response.data.accessToken);
          // Set a partial user since login response returns basic info
          this.currentUser.set({
            id: response.data.userId || 0,
            username: response.data.username || '',
            email: response.data.email || '',
            displayName: response.data.username || ''
          });
        }
      })
    );
  }

  logout() {
    this.removeToken();
    this.currentUser.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  private removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  private checkToken() {
    // Basic check for token existence on startup
    if (this.getToken()) {
      // Typically we'd fetch the user profile here, but for now we just want to avoid clearing it entirely
    }
  }
}
