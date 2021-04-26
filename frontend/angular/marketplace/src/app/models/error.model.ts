export class Error {

  constructor(
    public type: string = '',
    public message: string = '',
  ) {}

  static answered(errorMessage: string): Error {
    return new Error('answered', errorMessage);
  }

  static unanswered(): Error {
    return new Error('unanswered', 'Could not connect to the server. Is the server down?');
  }

  isAnswered(): boolean {
    return this.type === 'answered';
  }

  isUnanswered(): boolean {
    return this.type === 'unanswered';
  }

}
