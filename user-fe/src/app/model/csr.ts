export class Csr {
  constructor(
    public commonName: string = '',
    public givenName: string = '',
    public surname: string = '',
    public organization: string = '',
    public organizationalUnit: string = '',
    public country: string = ''
  ) {}
}
