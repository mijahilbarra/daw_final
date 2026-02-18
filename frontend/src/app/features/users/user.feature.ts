import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../repo/data-source.config';
import { createRepository } from '../../repo/repository.factory';
import { User } from './user.model';

@Injectable({ providedIn: 'root' })
export class UserFeature {
  private readonly repo = createRepository<User>('users');
  private readonly listeners = new Set<(user: User | null) => void>();
  private readonly sessionStorageKey = 'daw-current-user';

  watchAll(): Observable<User[]> {
    return this.repo.watchAll();
  }

  watchTransportistas(): Observable<User[]> {
    return this.repo.watchByField('userRole', 'transportista');
  }

  async login(email: string, password: string): Promise<User | null> {
    const response = await fetch(`${API_BASE_URL}/users/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
      throw new Error('Credenciales invalidas o servicio no disponible.');
    }

    const user = (await response.json()) as User;
    localStorage.setItem(this.sessionStorageKey, JSON.stringify(user));
    this.notify(user);
    return user;
  }

  async createTransportista(userName: string, userEmail: string, userPassword: string): Promise<string> {
    return this.repo.create({
      userName,
      userEmail,
      userPassword,
      userRole: 'transportista'
    });
  }

  async remove(id: string): Promise<void> {
    await this.repo.remove(id);
  }

  onAuthUserChanged(callback: (user: User | null) => void): () => void {
    this.listeners.add(callback);
    callback(this.readSessionUser());
    return () => this.listeners.delete(callback);
  }

  async logout(): Promise<void> {
    localStorage.removeItem(this.sessionStorageKey);
    this.notify(null);
  }

  private readSessionUser(): User | null {
    const raw = localStorage.getItem(this.sessionStorageKey);
    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw) as User;
    } catch {
      return null;
    }
  }

  private notify(user: User | null): void {
    this.listeners.forEach((listener) => listener(user));
  }
}
