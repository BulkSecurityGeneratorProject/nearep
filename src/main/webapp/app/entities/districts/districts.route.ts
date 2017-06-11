import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { DistrictsComponent } from './districts.component';
import { DistrictsDetailComponent } from './districts-detail.component';
import { DistrictsPopupComponent } from './districts-dialog.component';
import { DistrictsDeletePopupComponent } from './districts-delete-dialog.component';

import { Principal } from '../../shared';

export const districtsRoute: Routes = [
    {
        path: 'districts',
        component: DistrictsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.districts.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'districts/:id',
        component: DistrictsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.districts.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const districtsPopupRoute: Routes = [
    {
        path: 'districts-new',
        component: DistrictsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.districts.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'districts/:id/edit',
        component: DistrictsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.districts.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'districts/:id/delete',
        component: DistrictsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.districts.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
