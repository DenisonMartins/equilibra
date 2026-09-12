import { Component, inject, OnInit } from '@angular/core';
import { FinanceiroService } from '../../../core/services/financeiro.service';
import { AiAdvisorService } from '../../../core/services/ai-advisor.service';

@Component({
  selector: 'app-ai-advisor',
  standalone: true,
  imports: [],
  templateUrl: './ai-advisor.html',
  styleUrl: './ai-advisor.scss',
})
export class AiAdvisor implements OnInit {
  aiService = inject(AiAdvisorService);
  financeiroService = inject(FinanceiroService);

  ngOnInit(): void {
    if (!this.aiService.diagnostico()) {
      this.aiService.gerarDiagnostico();
    }
  }
}
