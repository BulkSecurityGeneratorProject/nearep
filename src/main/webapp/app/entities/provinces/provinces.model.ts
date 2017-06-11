import { Countries } from '../countries';
export class Provinces {
    constructor(
        public id?: number,
        public provinceName?: string,
        public countryName?: Countries,
    ) {
    }
}
