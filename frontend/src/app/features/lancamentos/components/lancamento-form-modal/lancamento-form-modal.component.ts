import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  CategoriaItem,
  LancamentoItem,
  LancamentoRequest,
  TipoLancamento,
} from '../../models/lancamento.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-lancamento-form-modal',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './lancamento-form-modal.component.html',
  styleUrl: './lancamento-form-modal.component.scss',
})
export class LancamentoFormModalComponent implements OnInit {
  private fb = inject(FormBuilder);

  // Inputs e Outputs
  aberto = input<boolean>(false);
  lancamentoEdicao = input<LancamentoItem | null>(null);
  categorias = input<CategoriaItem[]>([]);

  salvar = output<LancamentoRequest>();
  fechar = output<void>();

  salvando = signal<boolean>(false);

  form = this.fb.group({
    descricao: ['', [Validators.required, Validators.minLength(3)]],
    valor: [null as number | null, [Validators.required, Validators.min(0.01)]],
    data: [new Date().toISOString().substring(0, 10), [Validators.required]],
    tipo: ['DESPESA' as TipoLancamento, [Validators.required]],
    categoriaId: ['', [Validators.required]],
    pago: [false, [Validators.required]],
  });

  ngOnInit(): void {
    const item = this.lancamentoEdicao();
    if (item) {
      this.form.patchValue({
        descricao: item.descricao,
        valor: item.valor,
        data: item.data,
        tipo: item.tipo,
        categoriaId: item.categoria.id,
        pago: item.pago,
      });
    }
  }

  setTipo(tipo: TipoLancamento): void {
    this.form.patchValue({ tipo });
  }

  submeter(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const val = this.form.getRawValue();
    const payload: LancamentoRequest = {
      descricao: val.descricao!.trim(),
      valor: Number(val.valor),
      data: val.data!,
      tipo: val.tipo!,
      categoriaId: val.categoriaId!,
      pago: val.pago!,
    };

    this.salvar.emit(payload);
  }
}
