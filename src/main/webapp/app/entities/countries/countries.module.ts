import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NearepSharedModule } from '../../shared';
import {
    CountriesService,
    CountriesPopupService,
    CountriesComponent,
    CountriesDetailComponent,
    CountriesDialogComponent,
    CountriesPopupComponent,
    CountriesDeletePopupComponent,
    CountriesDeleteDialogComponent,
    countriesRoute,
    countriesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...countriesRoute,
    ...countriesPopupRoute,
];

@NgModule({
    imports: [
        NearepSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CountriesComponent,
        CountriesDetailComponent,
        CountriesDialogComponent,
        CountriesDeleteDialogComponent,
        CountriesPopupComponent,
        CountriesDeletePopupComponent,
    ],
    entryComponents: [
        CountriesComponent,
        CountriesDialogComponent,
        CountriesPopupComponent,
        CountriesDeleteDialogComponent,
        CountriesDeletePopupComponent,
    ],
    providers: [
        CountriesService,
        CountriesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NearepCountriesModule {}
