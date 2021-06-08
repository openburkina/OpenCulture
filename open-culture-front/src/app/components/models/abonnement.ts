import {InformationCivilDTO} from "./infos.model";
import {User} from "./User";

export class Abonnement {
    constructor(
        public id?: number,
        public dateAbonnement?: string,
        public type?: string,
        public statut?: boolean,
        public user? : User,
        public phoneNumber? : string,
    ){}
}
