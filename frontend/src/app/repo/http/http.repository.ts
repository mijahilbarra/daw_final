import { Observable, Subject, from, merge, timer } from 'rxjs';
import { mapTo, switchMap } from 'rxjs/operators';
import { API_BASE_URL } from '../data-source.config';
import { FieldMap, RepositoryPto } from '../pto/repository-pto';

export class HttpRepository<T extends { id?: string }> implements RepositoryPto<T> {
  private readonly refresh$ = new Subject<void>();

  constructor(private readonly resource: string) {}

  watchAll(): Observable<T[]> {
    return this.stream(() => this.getJson<T[]>(this.url()));
  }

  watchByField(field: string, value: string | number | boolean | null): Observable<T[]> {
    const params = new URLSearchParams();
    params.set(field, String(value));
    return this.stream(() => this.getJson<T[]>(`${this.url()}?${params.toString()}`));
  }

  async findByFields(filters: FieldMap): Promise<T[]> {
    const params = new URLSearchParams();

    Object.entries(filters).forEach(([key, value]) => {
      if (value !== undefined) {
        params.set(key, String(value));
      }
    });

    const path = params.size > 0 ? `${this.url()}?${params.toString()}` : this.url();
    return this.getJson<T[]>(path);
  }

  async create(data: Omit<T, 'id'>): Promise<string> {
    const created = await this.sendJson<T>(this.url(), 'POST', data);
    this.notifyRefresh();
    return created.id ?? '';
  }

  async update(id: string, data: Partial<T>): Promise<void> {
    await this.sendJson(`${this.url()}/${id}`, 'PATCH', data);
    this.notifyRefresh();
  }

  async remove(id: string): Promise<void> {
    await this.sendJson(`${this.url()}/${id}`, 'DELETE');
    this.notifyRefresh();
  }

  private url(): string {
    return `${API_BASE_URL}/${this.resource}`;
  }

  private async getJson<R>(url: string): Promise<R> {
    const response = await fetch(url);

    if (!response.ok) {
      throw new Error(`GET ${url} failed with ${response.status}`);
    }

    return (await response.json()) as R;
  }

  private async sendJson<R>(url: string, method: string, body?: unknown): Promise<R> {
    const response = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: body ? JSON.stringify(body) : undefined
    });

    if (!response.ok) {
      throw new Error(`${method} ${url} failed with ${response.status}`);
    }

    if (response.status === 204) {
      return {} as R;
    }

    const raw = await response.text();

    if (!raw) {
      return {} as R;
    }

    try {
      return JSON.parse(raw) as R;
    } catch {
      return {} as R;
    }
  }

  private stream<R>(loader: () => Promise<R>): Observable<R> {
    return merge(timer(0, 5000), this.refresh$.pipe(mapTo(0))).pipe(switchMap(() => from(loader())));
  }

  private notifyRefresh(): void {
    this.refresh$.next();
  }
}
