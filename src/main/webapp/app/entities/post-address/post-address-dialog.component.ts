import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { PostAddress } from './post-address.model';
import { PostAddressPopupService } from './post-address-popup.service';
import { PostAddressService } from './post-address.service';
import { Districts, DistrictsService } from '../districts';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-post-address-dialog',
    templateUrl: './post-address-dialog.component.html'
})
export class PostAddressDialogComponent implements OnInit {

    postAddress: PostAddress;
    authorities: any[];
    isSaving: boolean;

    districts: Districts[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private postAddressService: PostAddressService,
        private districtsService: DistrictsService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.districtsService.query()
            .subscribe((res: ResponseWrapper) => { this.districts = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.postAddress.id !== undefined) {
            this.subscribeToSaveResponse(
                this.postAddressService.update(this.postAddress), false);
        } else {
            this.subscribeToSaveResponse(
                this.postAddressService.create(this.postAddress), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<PostAddress>, isCreated: boolean) {
        result.subscribe((res: PostAddress) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PostAddress, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'nearepApp.postAddress.created'
            : 'nearepApp.postAddress.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'postAddressListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackDistrictsById(index: number, item: Districts) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-post-address-popup',
    template: ''
})
export class PostAddressPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private postAddressPopupService: PostAddressPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.postAddressPopupService
                    .open(PostAddressDialogComponent, params['id']);
            } else {
                this.modalRef = this.postAddressPopupService
                    .open(PostAddressDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
