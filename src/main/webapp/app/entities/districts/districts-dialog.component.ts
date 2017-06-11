import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Districts } from './districts.model';
import { DistrictsPopupService } from './districts-popup.service';
import { DistrictsService } from './districts.service';
import { Cities, CitiesService } from '../cities';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-districts-dialog',
    templateUrl: './districts-dialog.component.html'
})
export class DistrictsDialogComponent implements OnInit {

    districts: Districts;
    authorities: any[];
    isSaving: boolean;

    cities: Cities[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private districtsService: DistrictsService,
        private citiesService: CitiesService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.citiesService.query()
            .subscribe((res: ResponseWrapper) => { this.cities = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.districts.id !== undefined) {
            this.subscribeToSaveResponse(
                this.districtsService.update(this.districts), false);
        } else {
            this.subscribeToSaveResponse(
                this.districtsService.create(this.districts), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Districts>, isCreated: boolean) {
        result.subscribe((res: Districts) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Districts, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'nearepApp.districts.created'
            : 'nearepApp.districts.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'districtsListModification', content: 'OK'});
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

    trackCitiesById(index: number, item: Cities) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-districts-popup',
    template: ''
})
export class DistrictsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private districtsPopupService: DistrictsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.districtsPopupService
                    .open(DistrictsDialogComponent, params['id']);
            } else {
                this.modalRef = this.districtsPopupService
                    .open(DistrictsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
