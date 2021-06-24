import {TypeRegroupementDTO} from './type-regroupement.model';

export class RegroupementDTO {
    constructor(
        public id?: number,
        public typeRegroupementDTO?: TypeRegroupementDTO,
        public typeRegroupementId?: number,
        public intitule?: string

    ){}
}
