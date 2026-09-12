import { Component, inject } from '@angular/core';
import { AuthService } from '../../auth/services/auth.service';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-shell',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './shell.component.html',
  styleUrl: './shell.component.scss',
})
export class ShellComponent {
  authService = inject(AuthService);

  sair(): void {
    this.authService.logout();
  }
}
