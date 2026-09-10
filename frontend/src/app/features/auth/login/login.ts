import { Component, inject, signal } from '@angular/core';
import { Auth } from '../../../core/services/auth';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private auth = inject(Auth);

  email = signal('denison@exemplo.com');
  password = signal('123456');
  isAdminMock = signal(false);

  handleLogin(): void {
    this.auth.loginMock(this.email(), this.isAdminMock());
  }
}
