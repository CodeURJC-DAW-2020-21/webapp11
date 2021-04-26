export class Product {
  constructor(
    public id: number,
    public category: string,
    public price: number,
    public hourlyPrice: number,
    public ram: string,
    public cores: string,
    public storage: string,
    public transfer: string,
  ) {}
}
