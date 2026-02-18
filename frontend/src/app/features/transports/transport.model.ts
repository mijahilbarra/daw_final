export interface Transport {
  id?: string;
  transportUserId: string | null;
  transportType: string;
  transportCapacity: number;
  transportStatus: string;
  transportLocation: string;
  transportDriver: string;
  transportLicensePlate: string;
  transportCompany: string;
}
