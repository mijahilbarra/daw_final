import { CommonModule } from '@angular/common';
import { Component, Input, OnChanges, OnDestroy, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { ShipmentFeature } from '../../features/shipments/shipment.feature';
import { Shipment } from '../../features/shipments/shipment.model';
import { StatusFeature } from '../../features/statuses/status.feature';
import { ShipmentStatus } from '../../features/statuses/status.model';
import { TransportFeature } from '../../features/transports/transport.feature';
import { Transport } from '../../features/transports/transport.model';
import { User } from '../../features/users/user.model';

@Component({
  selector: 'app-transportista-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './transportista-page.html',
  styleUrl: './transportista-page.css'
})
export class TransportistaPageComponent implements OnChanges, OnDestroy {
  @Input({ required: true }) currentUser: User | null = null;

  transports: Transport[] = [];
  allShipments: Shipment[] = [];
  assignedShipments: Shipment[] = [];
  statuses: ShipmentStatus[] = [];
  selectedStatusByShipment: Record<string, string> = {};

  lastError = '';
  lastSuccess = '';

  private readonly fixedSubscriptions = new Subscription();
  private dynamicSubscriptions = new Subscription();

  constructor(
    private readonly transportFeature: TransportFeature,
    private readonly shipmentFeature: ShipmentFeature,
    private readonly statusFeature: StatusFeature
  ) {
    this.fixedSubscriptions.add(this.statusFeature.watchAll().subscribe((rows) => (this.statuses = rows)));
    this.fixedSubscriptions.add(
      this.shipmentFeature.watchAll().subscribe((rows) => {
        this.allShipments = rows;
        this.rebuildAssignedShipments();
      })
    );
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['currentUser']) {
      this.connectAssignedTransportStream();
    }
  }

  ngOnDestroy(): void {
    this.dynamicSubscriptions.unsubscribe();
    this.fixedSubscriptions.unsubscribe();
  }

  async changeStatus(shipmentId: string): Promise<void> {
    const statusId = this.selectedStatusByShipment[shipmentId];

    if (!statusId) {
      this.lastError = 'Selecciona un estado.';
      this.lastSuccess = '';
      return;
    }

    this.lastError = '';
    this.lastSuccess = '';

    try {
      await this.shipmentFeature.changeStatus(shipmentId, statusId);
      this.lastSuccess = 'Estado actualizado.';
    } catch (error) {
      this.lastError = error instanceof Error ? error.message : 'No se pudo actualizar el estado.';
    }
  }

  statusNameById(statusId: string): string {
    return this.statuses.find((row) => row.id === statusId)?.statusName ?? statusId;
  }

  private connectAssignedTransportStream(): void {
    this.dynamicSubscriptions.unsubscribe();
    this.dynamicSubscriptions = new Subscription();

    const userId = this.currentUser?.id;

    if (!userId) {
      this.transports = [];
      this.assignedShipments = [];
      return;
    }

    this.dynamicSubscriptions.add(
      this.transportFeature.watchAssignedToUser(userId).subscribe((rows) => {
        this.transports = rows;
        this.rebuildAssignedShipments();
      })
    );
  }

  private rebuildAssignedShipments(): void {
    const transportIds = new Set(this.transports.map((row) => row.id).filter((row): row is string => !!row));
    this.assignedShipments = this.allShipments.filter((row) => transportIds.has(row.shipmentTransportId));

    this.selectedStatusByShipment = {};
    this.assignedShipments.forEach((row) => {
      if (row.id) {
        this.selectedStatusByShipment[row.id] = row.shipmentStatusId;
      }
    });
  }
}
