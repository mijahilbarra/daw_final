export interface Shipment {
  id?: string;
  shipmentCategoryId: string;
  shipmentDescription: string;
  shipmentPrice: number;
  shipmentWeight: number;
  shipmentVolume: number;
  shipmentOrigin: string;
  shipmentDestination: string;
  shipmentStatusId: string;
  shipmentDate: string;
  shipmentClientId: string;
  shipmentTransportId: string;
}
