import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export interface ConfirmRequest {
  title: string;
  message: string;
  resolve: (value: boolean) => void;
}

@Injectable({ providedIn: 'root' })
export class ConfirmModalService {
  readonly request$ = new Subject<ConfirmRequest | null>();

  confirm(title: string, message = 'Esta acción no se puede deshacer.'): Promise<boolean> {
    return new Promise((resolve) => {
      this.request$.next({ title, message, resolve });
    });
  }
}
