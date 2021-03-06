import { ArtisteDTO } from "./artiste.model";
import { RegroupementDTO } from "./regroupement.model";
import { TypeOeuvreDTO } from "./type-oeuvre.model";
import { Moment } from "moment";
import {TypeFichier} from './enumeration/type-fichier.enum';
import DateTimeFormat = Intl.DateTimeFormat;
export class OeuvreDTO {
    constructor(
        public id?: number,
        public titre?: string,
        public fileName?: string,
        public fileUrl?: string,
        public fileExtension?: string,
        public pathFile?: string,
        public fileContent?: Blob,
        public resume?: string,
        public nomArtiste?: string,
        public typeOeuvreDTO?: TypeOeuvreDTO,
        public dateSortie?: Moment,
       // public dateSortie?: Date,
        public regroupementDTO?: RegroupementDTO,
        public artistes?: ArtisteDTO[],
        public typeOeuvreId?: number,
        public regroupementId?: number,
        public artisteId?: number,
        public typeFichier?: TypeFichier
    ){}
}
