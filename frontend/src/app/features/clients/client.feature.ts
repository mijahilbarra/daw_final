import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { createRepository } from '../../repo/repository.factory';
import { Client } from './client.model';

@Injectable({ providedIn: 'root' })
export class ClientFeature {
  private readonly repo = createRepository<Client>('clients');

  watchAll(): Observable<Client[]> {
    return this.repo.watchAll();
  }

  async create(data: Omit<Client, 'id'>): Promise<string> {
    return this.repo.create(data);
  }

  async remove(id: string): Promise<void> {
    await this.repo.remove(id);
  }
}
