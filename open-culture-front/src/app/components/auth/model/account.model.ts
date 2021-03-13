export class Account {
    constructor(
        public activated?: boolean,
        public login?: string,
        public firstName?: string,
        public lastName?: string,
        public imageUrl?: string,
        public email?: string,
        public langKey?: string,
        public authorities?: string[],
    ){}
}
