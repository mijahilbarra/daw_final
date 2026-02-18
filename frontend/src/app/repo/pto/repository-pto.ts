import { Observable } from 'rxjs';

export type FieldMap = Record<string, string | number | boolean | null | undefined>;

export interface RepositoryPto<T extends { id?: string }> {
  watchAll(): Observable<T[]>;
  watchByField(field: string, value: string | number | boolean | null): Observable<T[]>;
  findByFields(filters: FieldMap): Promise<T[]>;
  create(data: Omit<T, 'id'>): Promise<string>;
  update(id: string, data: Partial<T>): Promise<void>;
  remove(id: string): Promise<void>;
}
