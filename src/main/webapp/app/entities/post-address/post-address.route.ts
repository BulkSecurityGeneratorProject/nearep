import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { PostAddressComponent } from './post-address.component';
import { PostAddressDetailComponent } from './post-address-detail.component';
import { PostAddressPopupComponent } from './post-address-dialog.component';
import { PostAddressDeletePopupComponent } from './post-address-delete-dialog.component';

import { Principal } from '../../shared';

export const postAddressRoute: Routes = [
    {
        path: 'post-address',
        component: PostAddressComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.postAddress.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'post-address/:id',
        component: PostAddressDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.postAddress.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const postAddressPopupRoute: Routes = [
    {
        path: 'post-address-new',
        component: PostAddressPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.postAddress.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'post-address/:id/edit',
        component: PostAddressPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.postAddress.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'post-address/:id/delete',
        component: PostAddressDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nearepApp.postAddress.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
