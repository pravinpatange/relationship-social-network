import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Group {
  id: number;
  ownerUserId: number;
  groupName: string;
  description?: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private apiUrl = '/api/groups';

  constructor(private http: HttpClient) {}

  getGroups(): Observable<Group[]> {
    return this.http.get<Group[]>(this.apiUrl);
  }

  getGroup(id: number): Observable<Group> {
    return this.http.get<Group>(`${this.apiUrl}/${id}`);
  }

  createGroup(data: any): Observable<Group> {
    return this.http.post<Group>(this.apiUrl, data);
  }

  updateGroup(id: number, data: any): Observable<Group> {
    return this.http.put<Group>(`${this.apiUrl}/${id}`, data);
  }

  deleteGroup(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
