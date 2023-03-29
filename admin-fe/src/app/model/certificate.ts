export interface Certificate {
    algorithm: string;
    csrId: string;
    validityStart: string;
    validityEnd: string;
    extensions: any[];
}