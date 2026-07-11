import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './auth.service';

export interface Post {
  id: number;
  userId: number;
  caption: string;
  mediaUrl?: string;
  visibilityType: string;
  createdAt: string;
  user?: User; // joined data
  likeCount?: number;
  commentCount?: number;
  hasLiked?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = '/api/posts';
  private feedUrl = '/api/feed';

  constructor(private http: HttpClient) {}

  createPost(data: any): Observable<Post> {
    return this.http.post<Post>(this.apiUrl, data);
  }

  getPost(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/${id}`);
  }

  deletePost(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getFeed(mode?: string, groupId?: number): Observable<Post[]> {
    let params = new HttpParams();
    if (mode) params = params.set(mode, 'true');
    if (groupId) params = params.set('groupId', groupId.toString());
    
    return this.http.get<Post[]>(this.feedUrl, { params });
  }
}
