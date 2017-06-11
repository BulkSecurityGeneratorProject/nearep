import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CitiesComponent } from './cities.component';
import { CitiesDetailComponent } from './cities-detail.component';
import { CitiesPopupComponent } from './cities-dialog.component';
import { CitiesDeletePopupComponent } from './cities-delete-dialog.component';

import { Principal } from '../../shared';

export const citiesRoute: Routes = [
    {
        path: 'cities',
        component: CitiesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.cities.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cities/:id',
        component: CitiesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.cities.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const citiesPopupRoute: Routes = [
    {
        path: 'cities-new',
        component: CitiesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.cities.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cities/:id/edit',
        component: CitiesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.cities.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cities/:id/delete',
        component: CitiesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.cities.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
