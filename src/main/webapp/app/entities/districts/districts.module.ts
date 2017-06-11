import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NearepSharedModule } from '../../shared';
import {
    DistrictsService,
    DistrictsPopupService,
    DistrictsComponent,
    DistrictsDetailComponent,
    DistrictsDialogComponent,
    DistrictsPopupComponent,
    DistrictsDeletePopupComponent,
    DistrictsDeleteDialogComponent,
    districtsRoute,
    districtsPopupRoute,
} from './';

const ENTITY_STATES = [
    ...districtsRoute,
    ...districtsPopupRoute,
];

@NgModule({
    imports: [
        NearepSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        DistrictsComponent,
        DistrictsDetailComponent,
        DistrictsDialogComponent,
        DistrictsDeleteDialogComponent,
        DistrictsPopupComponent,
        DistrictsDeletePopupComponent,
    ],
    entryComponents: [
        DistrictsComponent,
        DistrictsDialogComponent,
        DistrictsPopupComponent,
        DistrictsDeleteDialogComponent,
        DistrictsDeletePopupComponent,
    ],
    providers: [
        DistrictsService,
        DistrictsPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NearepDistrictsModule {}
