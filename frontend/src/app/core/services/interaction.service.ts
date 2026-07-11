import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from './auth.service';

export interface Comment {
  id: number;
  postId: number;
  userId: number;
  commentText: string;
  createdAt: string;
  user?: User;
}

@Injectable({
  providedIn: 'root'
})
export class InteractionService {
  private commentsUrl = '/api/comments';
  private likesUrl = '/api/likes';

  constructor(private http: HttpClient) {}

  getComments(postId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.commentsUrl}?postId=${postId}`);
  }

  addComment(postId: number, text: string): Observable<Comment> {
    return this.http.post<Comment>(this.commentsUrl, { postId, commentText: text });
  }

  deleteComment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.commentsUrl}/${id}`);
  }

  likePost(postId: number): Observable<any> {
    return this.http.post(this.likesUrl, { postId });
  }

  unlikePost(postId: number): Observable<any> {
    return this.http.delete(`${this.likesUrl}?postId=${postId}`);
  }
}
