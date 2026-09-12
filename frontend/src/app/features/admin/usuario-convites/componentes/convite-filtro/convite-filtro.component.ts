import { Component, inject, output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { ConviteFilter } from '../../models/convite.model';

@Component({
  selector: 'app-convite-filtro',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './convite-filtro.component.html',
  styleUrl: './convite-filtro.component.scss',
})
export class ConviteFiltroComponent {
  private formBuilder = inject(FormBuilder);
  filtrar = output<ConviteFilter>();

  filtroForm = this.formBuilder.group({
    email: [''],
    utilizado: [null as boolean | null],
    criado_em_inicial: [''],
    criado_em_final: [''],
    expira_em_inicial: [''],
    expira_em_final: [''],
  });

  submeterFiltro(): void {
    const val = this.filtroForm.value;
    const filtro: ConviteFilter = {};
    if (val.email?.trim()) filtro.email = val.email.trim();
    if (val.utilizado !== null && val.utilizado !== undefined) filtro.utilizado = val.utilizado;
    if (val.criado_em_inicial) filtro.criado_em_inicial = val.criado_em_inicial;
    if (val.criado_em_final) filtro.criado_em_final = val.criado_em_final;
    if (val.expira_em_inicial) filtro.expira_em_inicial = val.expira_em_inicial;
    if (val.expira_em_final) filtro.expira_em_final = val.expira_em_final;

    this.filtrar.emit(filtro);
  }

  limpar(): void {
    this.filtroForm.reset({
      email: '',
      utilizado: null,
      criado_em_inicial: '',
      criado_em_final: '',
      expira_em_inicial: '',
      expira_em_final: '',
    });
    this.filtrar.emit({});
  }
}
