import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { createRepository } from '../../repo/repository.factory';
import { Shipment } from './shipment.model';

@Injectable({ providedIn: 'root' })
export class ShipmentFeature {
  private readonly repo = createRepository<Shipment>('shipments');

  watchAll(): Observable<Shipment[]> {
    return this.repo.watchAll();
  }

  watchByTransport(transportId: string): Observable<Shipment[]> {
    return this.repo.watchByField('shipmentTransportId', transportId);
  }

  async create(data: Omit<Shipment, 'id'>): Promise<string> {
    return this.repo.create(data);
  }

  async assignTransport(shipmentId: string, transportId: string): Promise<void> {
    await this.repo.update(shipmentId, { shipmentTransportId: transportId });
  }

  async changeStatus(shipmentId: string, statusId: string): Promise<void> {
    await this.repo.update(shipmentId, { shipmentStatusId: statusId });
  }

  async remove(id: string): Promise<void> {
    await this.repo.remove(id);
  }
}
