import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { createRepository } from '../../repo/repository.factory';
import { ShipmentStatus } from './status.model';

@Injectable({ providedIn: 'root' })
export class StatusFeature {
  private readonly repo = createRepository<ShipmentStatus>('statuses');

  watchAll(): Observable<ShipmentStatus[]> {
    return this.repo.watchAll();
  }

  async create(data: Omit<ShipmentStatus, 'id'>): Promise<string> {
    return this.repo.create(data);
  }

  async remove(id: string): Promise<void> {
    await this.repo.remove(id);
  }
}
