/**
 * A simple data transfer object representing the server's authentication response.
 */
export class AuthResponse {

  private readonly status: string;
  private readonly statusCode: number;
  private readonly content: any;

  constructor(status: string = '', statusCode: number = 0, content: any = '') {
    this.status = status;
    this.statusCode = statusCode;
    this.content = content;
  }

  get getStatus(): string {
    return this.status;
  }

  get getStatusCode(): number {
    return this.statusCode;
  }

  get getContent(): any {
    return this.content;
  }

}
