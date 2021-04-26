export class AuthResponse {

  constructor(
    public status: string = '',
    public statusCode: number = 0,
    public content: any = ''
  ) { }

}
