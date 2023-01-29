export interface Search {
    data?:    Datum[];
    success?: boolean;
}

export interface Datum {
    noteText?:  string;
    noteId?:    string;
    bookId?:    string;
    bookTitle?: string;
}