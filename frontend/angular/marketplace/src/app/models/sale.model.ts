export class Sale {

  constructor(
    public productId: number = -1,
    public bulkAmount: number = 5,
    public discountPercentage: number = 25,
    public discountedPrice: number = 0,
    public startDate: string = new Date().toISOString().substring(0, 10),
    public expiryDate: string = new Date().toISOString().substring(0, 10),
    public ram: string = '',
    public cores: string = '',
    public storage: string = '',
    public transfer: string = ''
  ) {}

}
