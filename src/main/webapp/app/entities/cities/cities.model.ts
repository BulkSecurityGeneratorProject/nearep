import { Provinces } from '../provinces';
export class Cities {
    constructor(
        public id?: number,
        public cityName?: string,
        public provinceName?: Provinces,
    ) {
    }
}
