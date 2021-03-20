import { InformationCivilDTO } from "./infos.model";

export class ArtisteDTO {
    constructor(
        public id?: number,
        public nom?: string,
        public prenom?: string,
        public informationCivilDTO?: InformationCivilDTO
    ){}
}
