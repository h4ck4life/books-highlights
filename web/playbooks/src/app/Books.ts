export interface Books {
    data?:    Datum[];
    success?: boolean;
}

export interface Datum {
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
    driveThumbnailLink?: string;
    driveWebViewLink?:   string;
    noteCount?:          number;
}