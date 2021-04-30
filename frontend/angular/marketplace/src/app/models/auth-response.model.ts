export class AuthResponse {

  constructor(
    public status: string = '',
    public statusCode: number = 0,
    public userId: number = 0,
    public token: string = ''
  ) { }

}
