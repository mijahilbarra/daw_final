import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { createRepository } from '../../repo/repository.factory';
import { Category } from './category.model';

@Injectable({ providedIn: 'root' })
export class CategoryFeature {
  private readonly repo = createRepository<Category>('categories');

  watchAll(): Observable<Category[]> {
    return this.repo.watchAll();
  }

  async create(data: Omit<Category, 'id'>): Promise<string> {
    return this.repo.create(data);
  }

  async remove(id: string): Promise<void> {
    await this.repo.remove(id);
  }
}
