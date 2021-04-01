import { Moment } from 'moment';

export class InformationCivilDTO {
    constructor(
        public id?: number,
        public nationalite?: string,
        public dateNaissance?: Moment,
        public numeroP?: string,
        public numeroS?: string,
        public lieuNaissance?: string
    ){}
}
