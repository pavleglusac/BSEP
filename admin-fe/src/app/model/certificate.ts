export interface Certificate {
    algorithm: string;
    csrId: string;
    validityStart: string;
    validityEnd: string;
    extensions: any[];
    hierarchyLevel: number;
    csr: Csr | undefined;
}

export interface Csr {
    id: string;
    commonName: string;
    surname: string;
    givenName: string;
    organization: string;
    organizationalUnit: string;
    state: string;
    country: string;
    email: string;
    creationDate: string;
    publicKey: string;
}