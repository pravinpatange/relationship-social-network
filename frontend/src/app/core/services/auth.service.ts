import { Injectable, signal, computed } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { Observable, tap } from "rxjs";
import { ApiResponse, AuthResponse, User } from "../models";

@Injectable({ providedIn: "root" })
export class AuthService {
  private readonly API = "http://localhost:8080/api";
  private _token = signal<string|null>(localStorage.getItem("token"));
  private _currentUser = signal<User|null>(null);

  readonly isLoggedIn = computed(() => !!this._token());
  readonly currentUser = this._currentUser.asReadonly();
  readonly token = this._token.asReadonly();

  constructor(private http: HttpClient, private router: Router) {
    if (this._token()) this.loadCurrentUser();
  }

  register(data: {username:string;email:string;password:string;displayName?:string}): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.API}/auth/register`, data)
      .pipe(tap(r => this.saveSession(r.data)));
  }

  login(data: {email:string;password:string}): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.API}/auth/login`, data)
      .pipe(tap(r => this.saveSession(r.data)));
  }

  refreshToken(refreshToken: string): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.API}/auth/refresh`, { refreshToken })
      .pipe(tap(r => {
        localStorage.setItem("token", r.data.accessToken);
        localStorage.setItem("refreshToken", r.data.refreshToken);
        this._token.set(r.data.accessToken);
      }));
  }

  logout(): void {
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    this._token.set(null);
    this._currentUser.set(null);
    this.router.navigate(["/login"]);
  }

  loadCurrentUser(): void {
    this.http.get<ApiResponse<User>>(`${this.API}/users/me`).subscribe(r => this._currentUser.set(r.data));
  }

  private saveSession(auth: AuthResponse): void {
    localStorage.setItem("token", auth.accessToken);
    localStorage.setItem("refreshToken", auth.refreshToken);
    this._token.set(auth.accessToken);
    this.loadCurrentUser();
  }
}