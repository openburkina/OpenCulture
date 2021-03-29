export class Account {
    constructor(
        public activated: boolean,
        public authorities: string[],
        public email: string,
        public firstName: string,
        public langKey: string,
        public lastName: string,
        public login: string,
        public imageUrl: string,
        public isRandomPassword?: string,
        public passwordChanged?: boolean,
        public canal?: string,
        public telephone?: string,
        public logo?: any,
        public logoContentType?: string,
        public password?: any,
        public createdBy?: string,
        public createdDate?: any,
    ) {}
}
