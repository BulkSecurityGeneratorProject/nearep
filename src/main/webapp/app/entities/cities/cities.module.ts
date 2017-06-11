import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NearepSharedModule } from '../../shared';
import {
    CitiesService,
    CitiesPopupService,
    CitiesComponent,
    CitiesDetailComponent,
    CitiesDialogComponent,
    CitiesPopupComponent,
    CitiesDeletePopupComponent,
    CitiesDeleteDialogComponent,
    citiesRoute,
    citiesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...citiesRoute,
    ...citiesPopupRoute,
];

@NgModule({
    imports: [
        NearepSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CitiesComponent,
        CitiesDetailComponent,
        CitiesDialogComponent,
        CitiesDeleteDialogComponent,
        CitiesPopupComponent,
        CitiesDeletePopupComponent,
    ],
    entryComponents: [
        CitiesComponent,
        CitiesDialogComponent,
        CitiesPopupComponent,
        CitiesDeleteDialogComponent,
        CitiesDeletePopupComponent,
    ],
    providers: [
        CitiesService,
        CitiesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NearepCitiesModule {}
