
export class User {
    constructor(
        public id?: number,
        public login?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public telephone?: string,
        public activated?: boolean,
        public authorities?: string[],
        public password?: any,
        public createdBy?: string,
        public createdDate?: any,
    ) {}
}
