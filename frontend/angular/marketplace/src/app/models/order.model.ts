export class Order {

  constructor(
    public orderId = -1,
    public category = '',
    public purchaseDate = '',
    public expirationDate = '',
    public isExpired = false,
    public ram = '',
    public cores = '',
    public storage = '',
    public transfer = ''
  ) {}

}
