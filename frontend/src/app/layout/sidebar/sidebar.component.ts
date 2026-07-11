import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { ModeService, ActiveMode } from '../../core/services/mode.service';
import { GroupService, Group } from '../../core/services/group.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html'
})
export class SidebarComponent implements OnInit {
  public modeService = inject(ModeService);
  public groupService = inject(GroupService);
  public authService = inject(AuthService);
  public router = inject(Router);

  groups: Group[] = [];
  currentMode: ActiveMode = { userId: 0, activeMode: 'ALL' };

  ngOnInit() {
    this.modeService.currentMode().userId ? 
      this.currentMode = this.modeService.currentMode() : 
      this.modeService.getCurrentMode().subscribe();

    this.groupService.getGroups().subscribe({
      next: (groups) => this.groups = groups,
      error: () => console.log('Could not load groups for sidebar')
    });
  }

  setMode(modeName: string, groupId?: number) {
    this.modeService.changeMode(groupId || null, modeName).subscribe();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
