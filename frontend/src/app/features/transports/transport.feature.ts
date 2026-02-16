import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { createRepository } from '../../repo/repository.factory';
import { Transport } from './transport.model';

@Injectable({ providedIn: 'root' })
export class TransportFeature {
  private readonly repo = createRepository<Transport>('transports');

  watchAll(): Observable<Transport[]> {
    return this.repo.watchAll();
  }

  watchAssignedToUser(userId: string): Observable<Transport[]> {
    return this.repo.watchByField('transportUserId', userId);
  }

  async create(data: Omit<Transport, 'id'>): Promise<string> {
    return this.repo.create(data);
  }

  async assignToUser(transportId: string, userId: string): Promise<void> {
    await this.repo.update(transportId, { transportUserId: userId });
  }

  async remove(id: string): Promise<void> {
    await this.repo.remove(id);
  }
}
