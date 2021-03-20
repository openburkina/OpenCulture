import { ArtisteDTO } from "./artiste.model";
import { RegroupementDTO } from "./regroupement.model";
import { TypeOeuvreDTO } from "./type-oeuvre.model";

export class OeuvreDTO {
    constructor(
        public id?: number,
        public titre?: string,
        public fileName?: string,
        public fileExtension?: string,
        public pathFile?: string,
        public fileContent?: Blob,
        public resume?: string,
        public nomArtiste?: string,
        public typeOeuvreDTO?: TypeOeuvreDTO,
        public dateSortie?: Date,
        public regroupementDTO?: RegroupementDTO,
        public artisteDTO?: ArtisteDTO
    ){}
}
