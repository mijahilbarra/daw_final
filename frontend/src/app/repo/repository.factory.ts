import { HttpRepository } from './http/http.repository';
import { RepositoryPto } from './pto/repository-pto';

export function createRepository<T extends { id?: string }>(resource: string): RepositoryPto<T> {
  return new HttpRepository<T>(resource);
}
