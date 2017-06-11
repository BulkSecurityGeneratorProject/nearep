import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NearepSharedModule } from '../../shared';
import {
    ProvincesService,
    ProvincesPopupService,
    ProvincesComponent,
    ProvincesDetailComponent,
    ProvincesDialogComponent,
    ProvincesPopupComponent,
    ProvincesDeletePopupComponent,
    ProvincesDeleteDialogComponent,
    provincesRoute,
    provincesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...provincesRoute,
    ...provincesPopupRoute,
];

@NgModule({
    imports: [
        NearepSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProvincesComponent,
        ProvincesDetailComponent,
        ProvincesDialogComponent,
        ProvincesDeleteDialogComponent,
        ProvincesPopupComponent,
        ProvincesDeletePopupComponent,
    ],
    entryComponents: [
        ProvincesComponent,
        ProvincesDialogComponent,
        ProvincesPopupComponent,
        ProvincesDeleteDialogComponent,
        ProvincesDeletePopupComponent,
    ],
    providers: [
        ProvincesService,
        ProvincesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NearepProvincesModule {}
