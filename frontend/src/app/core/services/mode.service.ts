import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface ActiveMode {
  userId: number;
  activeGroupId?: number;
  activeMode: string; // 'ALL', 'FAMILY', 'CORPORATE', etc.
}

@Injectable({
  providedIn: 'root'
})
export class ModeService {
  private apiUrl = '/api/mode';
  
  public currentMode = signal<ActiveMode>({ userId: 0, activeMode: 'ALL' });

  constructor(private http: HttpClient) {}

  getCurrentMode(): Observable<ActiveMode> {
    return this.http.get<ActiveMode>(`${this.apiUrl}/current`).pipe(
      tap(mode => {
        if (mode) this.currentMode.set(mode);
      })
    );
  }

  changeMode(groupId: number | null, modeName: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/change`, { groupId, modeName }).pipe(
      tap(() => {
        // Optimistically update the signal
        const user = this.currentMode().userId;
        this.currentMode.set({ userId: user, activeGroupId: groupId || undefined, activeMode: modeName });
      })
    );
  }
}
