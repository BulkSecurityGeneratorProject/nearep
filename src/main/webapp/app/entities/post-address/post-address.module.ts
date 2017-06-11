import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NearepSharedModule } from '../../shared';
import {
    PostAddressService,
    PostAddressPopupService,
    PostAddressComponent,
    PostAddressDetailComponent,
    PostAddressDialogComponent,
    PostAddressPopupComponent,
    PostAddressDeletePopupComponent,
    PostAddressDeleteDialogComponent,
    postAddressRoute,
    postAddressPopupRoute,
} from './';

const ENTITY_STATES = [
    ...postAddressRoute,
    ...postAddressPopupRoute,
];

@NgModule({
    imports: [
        NearepSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PostAddressComponent,
        PostAddressDetailComponent,
        PostAddressDialogComponent,
        PostAddressDeleteDialogComponent,
        PostAddressPopupComponent,
        PostAddressDeletePopupComponent,
    ],
    entryComponents: [
        PostAddressComponent,
        PostAddressDialogComponent,
        PostAddressPopupComponent,
        PostAddressDeleteDialogComponent,
        PostAddressDeletePopupComponent,
    ],
    providers: [
        PostAddressService,
        PostAddressPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NearepPostAddressModule {}
