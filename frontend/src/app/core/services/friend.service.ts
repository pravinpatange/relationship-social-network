import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './auth.service';

export interface Friendship {
  id: number;
  requesterId: number;
  receiverId: number;
  status: string; // PENDING, ACCEPTED, REJECTED, BLOCKED
  createdAt: string;
  friendDetails?: User;
}

@Injectable({
  providedIn: 'root'
})
export class FriendService {
  private apiUrl = '/api/friends';
  private followUrl = '/api/follow';

  constructor(private http: HttpClient) {}

  getFriends(): Observable<Friendship[]> {
    return this.http.get<Friendship[]>(this.apiUrl);
  }

  sendRequest(userId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/request`, { userId });
  }

  acceptRequest(friendshipId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/accept`, { friendshipId });
  }

  rejectRequest(friendshipId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/reject`, { friendshipId });
  }

  getFriendGroups(friendId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${friendId}/groups`);
  }

  assignFriendToGroups(friendId: number, groupIds: number[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/${friendId}/groups`, { groupIds });
  }

  // Followers APIs
  follow(userId: number): Observable<any> {
    return this.http.post(`${this.followUrl}/${userId}`, {});
  }

  unfollow(userId: number): Observable<any> {
    return this.http.delete(`${this.followUrl}/${userId}`);
  }

  getFollowers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/followers`);
  }

  getFollowing(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/following`);
  }
}
