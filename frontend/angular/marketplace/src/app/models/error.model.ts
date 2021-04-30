export class Error {

  constructor(
    public type: string = '',
    public message: string = '',
  ) {}

  static fromErrorResponse(errorResponse: any): Error {
    if (errorResponse.status === 400) { return Error.badRequest(); }
    if (errorResponse.status === 401) { return Error.unauthorized(); }
    if (errorResponse.status === 404) { return Error.notFound(); }
    if (errorResponse.status !== 0) { return Error.answered(errorResponse.error.content); }
    return Error.unanswered();
  }

  static answered(errorMessage: string): Error {
    return new Error('answered', errorMessage);
  }

  static unanswered(): Error {
    return new Error('unanswered', 'Could not connect to the server. Is the server down?');
  }

  static unauthorized(): Error {
    return new Error('unauthorized', 'The requester does not have permissions to perform this action');
  }

  static notFound(): Error {
    return new Error('not_found', 'The resource was not found');
  }

  static badRequest(): Error {
    return new Error('bad_request', 'Bad request');
  }

  isAnswered(): boolean {
    return this.type === 'answered';
  }

  isUnanswered(): boolean {
    return this.type === 'unanswered';
  }

  isUnauthorized(): boolean {
    return this.type === 'unauthorized';
  }

  isNotFound(): boolean {
    return this.type === 'not_found';
  }

  isBadRequest(): boolean {
    return this.type === 'bad_request';
  }

}
