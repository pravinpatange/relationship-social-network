import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { map, tap } from "rxjs/operators";
import { ApiResponse, Friendship } from "../models";

@Injectable({ providedIn: "root" })
export class DebugService {
  constructor(private http: HttpClient) {}

  debugFriends() {
    this.http.get('http://localhost:8080/api/friends').subscribe(r => console.log('DEBUG FRIENDS:', r));
  }
}
