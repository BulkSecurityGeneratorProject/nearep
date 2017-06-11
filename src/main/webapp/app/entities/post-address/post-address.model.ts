import { Districts } from '../districts';
export class PostAddress {
    constructor(
        public id?: number,
        public streetAddress?: string,
        public officeTel?: string,
        public officeFax?: string,
        public officeEmail?: string,
        public districtName?: Districts,
    ) {
    }
}
