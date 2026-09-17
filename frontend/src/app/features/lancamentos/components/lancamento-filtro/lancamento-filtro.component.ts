import { Component, inject, output } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { LancamentoFilter, TipoLancamento } from '../../models/lancamento.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-lancamento-filtro',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './lancamento-filtro.component.html',
  styleUrl: './lancamento-filtro.component.scss',
})
export class LancamentoFiltroComponent {
  private fb = inject(FormBuilder);
  filtrar = output<LancamentoFilter>();

  filtroForm = this.fb.group({
    descricao: [''],
    tipo: ['' as TipoLancamento | ''],
    dataInicio: [''],
    dataFim: [''],
  });

  submeter(): void {
    const val = this.filtroForm.value;
    const filtro: LancamentoFilter = {};
    if (val.descricao?.trim()) filtro.descricao = val.descricao.trim();
    if (val.tipo) filtro.tipo = val.tipo as TipoLancamento;
    if (val.dataInicio) filtro.dataInicio = val.dataInicio;
    if (val.dataFim) filtro.dataFim = val.dataFim;

    this.filtrar.emit(filtro);
  }

  limpar(): void {
    this.filtroForm.reset({ descricao: '', tipo: '', dataInicio: '', dataFim: '' });
    this.filtrar.emit({});
  }
}
