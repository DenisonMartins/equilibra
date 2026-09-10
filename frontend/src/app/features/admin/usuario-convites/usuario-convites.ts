import { Component, inject, signal } from '@angular/core';
import { Auth } from '../../../core/services/auth';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-usuario-convites',
  standalone: true,
  imports: [FormsModule, DatePipe],
  templateUrl: './usuario-convites.html',
  styleUrl: './usuario-convites.scss',
})
export class UsuarioConvites {
  authService = inject(Auth);

  modalAberto = signal(false);
  emailNovoConvite = signal('');
  ultimoLinkGerado = signal<string | null>(null);

  enviarConvite(): void {
    if (!this.emailNovoConvite()) {
      return;
    }

    const token = this.authService.enviarConviteMock(this.emailNovoConvite());
    const linkCompleto = `${window.location.origin}/convite?token=${token}`;

    this.ultimoLinkGerado.set(linkCompleto);
    this.emailNovoConvite.set('');
    this.modalAberto.set(false);
  }

  copiarLinkConstruido(token: string): void {
    const link = `${window.location.origin}/convite?token=${token}`;
    this.copiarLink(link);
  }

  copiarLink(texto: string) {
    navigator.clipboard.writeText(texto);
  }
}
