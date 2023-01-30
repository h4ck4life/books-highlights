export interface GoogleBookInfo {
    kind?:       string;
    totalItems?: number;
    items?:      Item[];
}

export interface Item {
    kind?:       Kind;
    id?:         string;
    etag?:       string;
    selfLink?:   string;
    volumeInfo?: VolumeInfo;
    saleInfo?:   SaleInfo;
    accessInfo?: AccessInfo;
    searchInfo?: SearchInfo;
}

export interface AccessInfo {
    country?:                Country;
    viewability?:            Viewability;
    embeddable?:             boolean;
    publicDomain?:           boolean;
    textToSpeechPermission?: TextToSpeechPermission;
    epub?:                   Epub;
    pdf?:                    PDF;
    webReaderLink?:          string;
    accessViewStatus?:       AccessViewStatus;
    quoteSharingAllowed?:    boolean;
}

export type AccessViewStatus = "SAMPLE" | "FULL_PUBLIC_DOMAIN";

export type Country = "MY";

export interface Epub {
    isAvailable?:  boolean;
    acsTokenLink?: string;
    downloadLink?: string;
}

export interface PDF {
    isAvailable?:  boolean;
    acsTokenLink?: string;
}

export type TextToSpeechPermission = "ALLOWED";

export type Viewability = "PARTIAL" | "ALL_PAGES";

export type Kind = "books#volume";

export interface SaleInfo {
    country?:     Country;
    saleability?: Saleability;
    isEbook?:     boolean;
    listPrice?:   SaleInfoListPrice;
    retailPrice?: SaleInfoListPrice;
    buyLink?:     string;
    offers?:      Offer[];
}

export interface SaleInfoListPrice {
    amount?:       number;
    currencyCode?: CurrencyCode;
}

export type CurrencyCode = "MYR";

export interface Offer {
    finskyOfferType?: number;
    listPrice?:       OfferListPrice;
    retailPrice?:     OfferListPrice;
}

export interface OfferListPrice {
    amountInMicros?: number;
    currencyCode?:   CurrencyCode;
}

export type Saleability = "NOT_FOR_SALE" | "FOR_SALE" | "FREE";

export interface SearchInfo {
    textSnippet?: string;
}

export interface VolumeInfo {
    title?:               string;
    authors?:             string[];
    publisher?:           string;
    publishedDate?:       string;
    description?:         string;
    industryIdentifiers?: IndustryIdentifier[];
    readingModes?:        ReadingModes;
    pageCount?:           number;
    printType?:           PrintType;
    categories?:          string[];
    maturityRating?:      MaturityRating;
    allowAnonLogging?:    boolean;
    contentVersion?:      string;
    panelizationSummary?: PanelizationSummary;
    imageLinks?:          ImageLinks;
    language?:            Language;
    previewLink?:         string;
    infoLink?:            string;
    canonicalVolumeLink?: string;
    subtitle?:            string;
    averageRating?:       number;
    ratingsCount?:        number;
}

export interface ImageLinks {
    smallThumbnail?: string;
    thumbnail?:      string;
}

export interface IndustryIdentifier {
    type?:       Type;
    identifier?: string;
}

export type Type = "ISBN_13" | "ISBN_10" | "OTHER";

export type Language = "en";

export type MaturityRating = "NOT_MATURE";

export interface PanelizationSummary {
    containsEpubBubbles?:  boolean;
    containsImageBubbles?: boolean;
}

export type PrintType = "BOOK";

export interface ReadingModes {
    text?:  boolean;
    image?: boolean;
}
