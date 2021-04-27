export class User {

  constructor(
    public id: number = 0,
    public firstName: string = '',
    public surname: string = '',
    public email: string = '',
    public enabled: boolean = true,
    public encodedImage: string = '',
    public isAdmin: boolean = false,
    public password: string = ''
  ) {}

  hasProfilePicture(): boolean {
    return this.encodedImage !== '';
  }

}
