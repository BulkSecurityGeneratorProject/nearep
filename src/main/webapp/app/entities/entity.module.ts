import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { NearepCountriesModule } from './countries/countries.module';
import { NearepProvincesModule } from './provinces/provinces.module';
import { NearepCitiesModule } from './cities/cities.module';
import { NearepDistrictsModule } from './districts/districts.module';
import { NearepPostAddressModule } from './post-address/post-address.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        NearepCountriesModule,
        NearepProvincesModule,
        NearepCitiesModule,
        NearepDistrictsModule,
        NearepPostAddressModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NearepEntityModule {}
