import { CommonModule } from '@angular/common';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { CategoryFeature } from '../../features/categories/category.feature';
import { Category } from '../../features/categories/category.model';
import { ClientFeature } from '../../features/clients/client.feature';
import { Client } from '../../features/clients/client.model';
import { ShipmentFeature } from '../../features/shipments/shipment.feature';
import { Shipment } from '../../features/shipments/shipment.model';
import { StatusFeature } from '../../features/statuses/status.feature';
import { ShipmentStatus } from '../../features/statuses/status.model';
import { TransportFeature } from '../../features/transports/transport.feature';
import { Transport } from '../../features/transports/transport.model';
import { ConfirmModalService } from '../../shared/confirm-modal/confirm-modal.service';
import { UserFeature } from '../../features/users/user.feature';
import { User } from '../../features/users/user.model';

type MaintenanceView =
  | 'transportistas'
  | 'transports'
  | 'clients'
  | 'categories'
  | 'statuses'
  | 'shipments'
  | 'assignments';

@Component({
  selector: 'app-admin-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-page.html',
  styleUrl: './admin-page.css'
})
export class AdminPageComponent implements OnInit, OnDestroy {
  @Input({ required: true }) currentUser: User | null = null;
  selectedView: MaintenanceView = 'transportistas';
  isLoading = false;
  private readonly minLoadingMs = 450;

  transportistas: User[] = [];
  transports: Transport[] = [];
  clients: Client[] = [];
  categories: Category[] = [];
  statuses: ShipmentStatus[] = [];
  shipments: Shipment[] = [];
  selectedStatusByShipment: Record<string, string> = {};

  newTransportista = { userName: '', userEmail: '', userPassword: '' };

  newTransport = {
    transportUserId: null as string | null,
    transportType: '',
    transportCapacity: 0,
    transportStatus: 'available',
    transportLocation: '',
    transportDriver: '',
    transportLicensePlate: '',
    transportCompany: ''
  };

  newClient = {
    companyCode: '',
    companyName: '',
    address: '',
    contactName: '',
    email: '',
    phone: ''
  };

  newCategory = { categoryName: '' };
  newStatus   = { statusName: '' };

  newShipment = {
    shipmentCategoryId: '',
    shipmentDescription: '',
    shipmentPrice: 0,
    shipmentWeight: 0,
    shipmentVolume: 0,
    shipmentOrigin: '',
    shipmentDestination: '',
    shipmentStatusId: '',
    shipmentDate: '',
    shipmentClientId: '',
    shipmentTransportId: ''
  };

  assignment = { transportId: '', userId: '' };

  lastError   = '';
  lastSuccess = '';

  private readonly subscriptions = new Subscription();
  private successTimer: ReturnType<typeof setTimeout> | null = null;

  constructor(
    private readonly userFeature: UserFeature,
    private readonly transportFeature: TransportFeature,
    private readonly clientFeature: ClientFeature,
    private readonly categoryFeature: CategoryFeature,
    private readonly statusFeature: StatusFeature,
    private readonly shipmentFeature: ShipmentFeature,
    private readonly confirmSvc: ConfirmModalService
  ) {}

  ngOnInit(): void {
    this.subscriptions.add(this.userFeature.watchTransportistas().subscribe((rows) => (this.transportistas = rows)));
    this.subscriptions.add(this.transportFeature.watchAll().subscribe((rows) => (this.transports = rows)));
    this.subscriptions.add(this.clientFeature.watchAll().subscribe((rows) => (this.clients = rows)));
    this.subscriptions.add(this.categoryFeature.watchAll().subscribe((rows) => (this.categories = rows)));
    this.subscriptions.add(this.statusFeature.watchAll().subscribe((rows) => (this.statuses = rows)));
    this.subscriptions.add(
      this.shipmentFeature.watchAll().subscribe((rows) => {
        this.shipments = rows;
        this.syncSelectedStatusMap();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    if (this.successTimer) clearTimeout(this.successTimer);
  }

  // ── Crear ──────────────────────────────────────────────────

  async createTransportista(): Promise<void> {
    await this.runMutation(async () => {
      await this.userFeature.createTransportista(
        this.newTransportista.userName,
        this.newTransportista.userEmail,
        this.newTransportista.userPassword
      );
      this.newTransportista = { userName: '', userEmail: '', userPassword: '' };
      this.showSuccess('Transportista creado.');
    });
  }

  async createTransport(): Promise<void> {
    await this.runMutation(async () => {
      await this.transportFeature.create(this.newTransport);
      this.newTransport = {
        transportUserId: null,
        transportType: '',
        transportCapacity: 0,
        transportStatus: 'available',
        transportLocation: '',
        transportDriver: '',
        transportLicensePlate: '',
        transportCompany: ''
      };
      this.showSuccess('Transporte creado.');
    });
  }

  async createClient(): Promise<void> {
    await this.runMutation(async () => {
      await this.clientFeature.create(this.newClient);
      this.newClient = { companyCode: '', companyName: '', address: '', contactName: '', email: '', phone: '' };
      this.showSuccess('Cliente creado.');
    });
  }

  async createCategory(): Promise<void> {
    await this.runMutation(async () => {
      await this.categoryFeature.create(this.newCategory);
      this.newCategory = { categoryName: '' };
      this.showSuccess('Categoria creada.');
    });
  }

  async createStatus(): Promise<void> {
    await this.runMutation(async () => {
      await this.statusFeature.create(this.newStatus);
      this.newStatus = { statusName: '' };
      this.showSuccess('Estado creado.');
    });
  }

  async createShipment(): Promise<void> {
    await this.runMutation(async () => {
      await this.shipmentFeature.create(this.newShipment);
      this.newShipment = {
        shipmentCategoryId: '',
        shipmentDescription: '',
        shipmentPrice: 0,
        shipmentWeight: 0,
        shipmentVolume: 0,
        shipmentOrigin: '',
        shipmentDestination: '',
        shipmentStatusId: '',
        shipmentDate: '',
        shipmentClientId: '',
        shipmentTransportId: ''
      };
      this.showSuccess('Envio creado.');
    });
  }

  // ── Asignar / cambiar estado ───────────────────────────────

  async assignTransport(): Promise<void> {
    if (!this.assignment.transportId || !this.assignment.userId) {
      this.lastError = 'Selecciona transporte y transportista.';
      this.lastSuccess = '';
      return;
    }

    this.clearMessages();

    try {
      await this.transportFeature.assignToUser(this.assignment.transportId, this.assignment.userId);
      this.assignment = { transportId: '', userId: '' };
      this.showSuccess('Transporte asignado.');
    } catch (error) {
      this.lastError = this.errorText(error);
    }
  }

  async changeShipmentStatus(shipmentId: string): Promise<void> {
    const statusId = this.selectedStatusByShipment[shipmentId];

    if (!statusId) {
      this.lastError = 'Selecciona un estado.';
      this.lastSuccess = '';
      return;
    }

    this.clearMessages();

    try {
      await this.shipmentFeature.changeStatus(shipmentId, statusId);
      this.showSuccess('Estado de envio actualizado.');
    } catch (error) {
      this.lastError = this.errorText(error);
    }
  }

  // ── Eliminar ───────────────────────────────────────────────

  async deleteTransportista(userId: string): Promise<void> {
    if (!(await this.confirmSvc.confirm('¿Eliminar este transportista?'))) return;
    await this.runMutation(async () => {
      await this.userFeature.remove(userId);
      this.showSuccess('Transportista eliminado.');
    });
  }

  async deleteTransport(transportId: string): Promise<void> {
    if (!(await this.confirmSvc.confirm('¿Eliminar este transporte?'))) return;
    await this.runMutation(async () => {
      await this.transportFeature.remove(transportId);
      this.showSuccess('Transporte eliminado.');
    });
  }

  async deleteClient(clientId: string): Promise<void> {
    if (!(await this.confirmSvc.confirm('¿Eliminar este cliente?'))) return;
    await this.runMutation(async () => {
      await this.clientFeature.remove(clientId);
      this.showSuccess('Cliente eliminado.');
    });
  }

  async deleteCategory(categoryId: string): Promise<void> {
    if (!(await this.confirmSvc.confirm('¿Eliminar esta categoría?'))) return;
    await this.runMutation(async () => {
      await this.categoryFeature.remove(categoryId);
      this.showSuccess('Categoria eliminada.');
    });
  }

  async deleteStatus(statusId: string): Promise<void> {
    if (!(await this.confirmSvc.confirm('¿Eliminar este estado?'))) return;
    await this.runMutation(async () => {
      await this.statusFeature.remove(statusId);
      this.showSuccess('Estado eliminado.');
    });
  }

  async deleteShipment(shipmentId: string): Promise<void> {
    if (!(await this.confirmSvc.confirm('¿Eliminar este envío?'))) return;
    await this.runMutation(async () => {
      await this.shipmentFeature.remove(shipmentId);
      this.showSuccess('Envio eliminado.');
    });
  }

  // ── Helpers públicos ───────────────────────────────────────

  statusNameById(statusId: string): string {
    return this.statuses.find((row) => row.id === statusId)?.statusName ?? statusId;
  }

  userNameById(userId: string | null): string {
    if (!userId) return 'Sin asignar';
    return this.transportistas.find((row) => row.id === userId)?.userName ?? 'No encontrado';
  }

  setView(view: MaintenanceView): void {
    if (this.isLoading) return;
    this.clearMessages();
    this.selectedView = view;
  }

  isView(view: MaintenanceView): boolean {
    return this.selectedView === view;
  }

  // ── Privados ───────────────────────────────────────────────

  private showSuccess(message: string): void {
    this.lastSuccess = message;
    if (this.successTimer) clearTimeout(this.successTimer);
    this.successTimer = setTimeout(() => { this.lastSuccess = ''; }, 4000);
  }

  private clearMessages(): void {
    this.lastError = '';
    this.lastSuccess = '';
    if (this.successTimer) clearTimeout(this.successTimer);
  }

  private async runMutation(task: () => Promise<void>): Promise<void> {
    if (this.isLoading) return;

    this.clearMessages();
    this.isLoading = true;
    const startedAt = Date.now();

    try {
      await task();
    } catch (error) {
      this.lastError = this.errorText(error);
    } finally {
      const elapsed = Date.now() - startedAt;
      if (elapsed < this.minLoadingMs) {
        await this.sleep(this.minLoadingMs - elapsed);
      }
      this.isLoading = false;
    }
  }

  private sleep(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  private syncSelectedStatusMap(): void {
    const next: Record<string, string> = {};
    this.shipments.forEach((row) => {
      if (!row.id) return;
      next[row.id] = this.selectedStatusByShipment[row.id] ?? row.shipmentStatusId;
    });
    this.selectedStatusByShipment = next;
  }

  private errorText(error: unknown): string {
    return error instanceof Error ? error.message : 'Ocurrio un error.';
  }
}
