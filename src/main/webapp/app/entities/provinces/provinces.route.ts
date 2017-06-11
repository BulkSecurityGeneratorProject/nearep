import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ProvincesComponent } from './provinces.component';
import { ProvincesDetailComponent } from './provinces-detail.component';
import { ProvincesPopupComponent } from './provinces-dialog.component';
import { ProvincesDeletePopupComponent } from './provinces-delete-dialog.component';

import { Principal } from '../../shared';

export const provincesRoute: Routes = [
    {
        path: 'provinces',
        component: ProvincesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.provinces.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'provinces/:id',
        component: ProvincesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.provinces.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const provincesPopupRoute: Routes = [
    {
        path: 'provinces-new',
        component: ProvincesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.provinces.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'provinces/:id/edit',
        component: ProvincesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.provinces.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'provinces/:id/delete',
        component: ProvincesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.provinces.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
