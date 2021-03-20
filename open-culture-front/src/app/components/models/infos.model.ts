export class InformationCivilDTO {
    constructor(
        public id?: number,
        public nationalite?: string,
        public dateNaissance?: Date,
        public numeroP?: string,
        public numeroS?: string,
        public lieuNaissance?: string
    ){}
}
