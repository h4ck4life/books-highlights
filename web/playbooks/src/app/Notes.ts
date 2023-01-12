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
    book?:              Book;
}

export interface Book {
    id?:                 number;
    driveId?:            string;
    bookTitle?:          string;
    bookAuthor?:         string;
    bookNotesCount?:     string;
    driveMimeType?:      string;
    driveModifiedTime?:  number;
    driveCreatedTime?:   number;
    driveFileName?:      string;
    driveFileSize?:      number;
    driveFileExtension?: null;
    driveWebViewLink?:   string;
    noteCount?:          number;
}