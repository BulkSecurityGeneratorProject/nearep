import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CountriesComponent } from './countries.component';
import { CountriesDetailComponent } from './countries-detail.component';
import { CountriesPopupComponent } from './countries-dialog.component';
import { CountriesDeletePopupComponent } from './countries-delete-dialog.component';

import { Principal } from '../../shared';

export const countriesRoute: Routes = [
    {
        path: 'countries',
        component: CountriesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.countries.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'countries/:id',
        component: CountriesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.countries.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const countriesPopupRoute: Routes = [
    {
        path: 'countries-new',
        component: CountriesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.countries.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'countries/:id/edit',
        component: CountriesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.countries.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'countries/:id/delete',
        component: CountriesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.countries.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
