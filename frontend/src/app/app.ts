import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AdminPageComponent } from './pages/admin/admin-page';
import { TransportistaPageComponent } from './pages/transportista/transportista-page';
import { ConfirmModalComponent } from './shared/confirm-modal/confirm-modal.component';
import { UserFeature } from './features/users/user.feature';
import { User } from './features/users/user.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, AdminPageComponent, TransportistaPageComponent, ConfirmModalComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit, OnDestroy {
  currentUser: User | null = null;
  currentView: 'login' | 'admin-maintenance' | 'transportista' = 'login';

  loginForm = {
    email: '',
    password: ''
  };

  lastError = '';
  isLoading = false;
  showPassword = false;
  private authListenerCleanup: (() => void) | null = null;
  private errorTimer: ReturnType<typeof setTimeout> | null = null;

  constructor(private readonly userFeature: UserFeature) {}

  ngOnInit(): void {
    this.authListenerCleanup = this.userFeature.onAuthUserChanged((user) => {
      this.currentUser = user;
      this.redirectByRole();
    });
  }

  ngOnDestroy(): void {
    this.authListenerCleanup?.();
    if (this.errorTimer) clearTimeout(this.errorTimer);
  }

  clearError(): void {
    this.lastError = '';
    if (this.errorTimer) clearTimeout(this.errorTimer);
  }

  private showError(message: string): void {
    this.lastError = message;
    if (this.errorTimer) clearTimeout(this.errorTimer);
    this.errorTimer = setTimeout(() => { this.lastError = ''; }, 5000);
  }

  async login(): Promise<void> {
    this.lastError = '';
    this.isLoading = true;

    try {
      const user = await this.userFeature.login(this.loginForm.email, this.loginForm.password);

      if (!user) {
        this.showError('No se encontro perfil de usuario en el backend.');
        this.currentView = 'login';
        return;
      }

      this.currentUser = user;
      this.redirectByRole();
    } catch (error) {
      this.showError(error instanceof Error ? error.message : 'No se pudo iniciar sesion.');
      this.currentView = 'login';
    } finally {
      this.isLoading = false;
    }
  }

  async logout(): Promise<void> {
    await this.userFeature.logout();
    this.loginForm = { email: '', password: '' };
    this.lastError = '';
    this.currentView = 'login';
  }

  private redirectByRole(): void {
    if (!this.currentUser) {
      this.currentView = 'login';
      return;
    }

    this.currentView = this.currentUser.userRole === 'admin' ? 'admin-maintenance' : 'transportista';
  }
}
