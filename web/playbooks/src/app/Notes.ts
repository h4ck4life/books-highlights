export interface Notes {
    data?:    Datum[];
    success?: boolean;
}

export interface Datum {
    id?:                number;
    getGoogleBookNote?: string;
    googleBookId?:      string;
    googleBookDate?:    null;
    noteUrl?:           string;
}