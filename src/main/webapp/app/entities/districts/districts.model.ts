import { Cities } from '../cities';
export class Districts {
    constructor(
        public id?: number,
        public districtName?: string,
        public postCode?: string,
        public cityName?: Cities,
    ) {
    }
}
