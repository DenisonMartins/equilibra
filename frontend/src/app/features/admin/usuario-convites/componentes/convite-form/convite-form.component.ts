import { Component, output, signal } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-convite-form',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './convite-form.component.html',
  styleUrl: './convite-form.component.scss',
})
export class ConviteFormComponent {
  enviar = output<string>();
  enviando = signal(false);
  emailControl = new FormControl('', {
    nonNullable: true,
    validators: [Validators.required, Validators.email],
  });

  submeter(): void {
    if (this.emailControl.valid) {
      this.enviar.emit(this.emailControl.value.trim());
      this.emailControl.reset();
    } else {
      this.emailControl.markAsTouched();
    }
  }
}
