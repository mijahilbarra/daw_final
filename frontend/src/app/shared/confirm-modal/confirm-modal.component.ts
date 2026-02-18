import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ConfirmModalService, ConfirmRequest } from './confirm-modal.service';

@Component({
  selector: 'app-confirm-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirm-modal.html',
  styleUrl: './confirm-modal.css'
})
export class ConfirmModalComponent implements OnInit, OnDestroy {
  current: ConfirmRequest | null = null;

  private readonly sub = new Subscription();

  constructor(private readonly confirmSvc: ConfirmModalService) {}

  ngOnInit(): void {
    this.sub.add(this.confirmSvc.request$.subscribe((req) => (this.current = req)));
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  respond(value: boolean): void {
    this.current?.resolve(value);
    this.current = null;
  }
}
