import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Cities } from './cities.model';
import { CitiesPopupService } from './cities-popup.service';
import { CitiesService } from './cities.service';
import { Provinces, ProvincesService } from '../provinces';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-cities-dialog',
    templateUrl: './cities-dialog.component.html'
})
export class CitiesDialogComponent implements OnInit {

    cities: Cities;
    authorities: any[];
    isSaving: boolean;

    provinces: Provinces[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private citiesService: CitiesService,
        private provincesService: ProvincesService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.provincesService.query()
            .subscribe((res: ResponseWrapper) => { this.provinces = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cities.id !== undefined) {
            this.subscribeToSaveResponse(
                this.citiesService.update(this.cities), false);
        } else {
            this.subscribeToSaveResponse(
                this.citiesService.create(this.cities), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Cities>, isCreated: boolean) {
        result.subscribe((res: Cities) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Cities, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'nearepApp.cities.created'
            : 'nearepApp.cities.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'citiesListModification', content: 'OK'});
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

    trackProvincesById(index: number, item: Provinces) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-cities-popup',
    template: ''
})
export class CitiesPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private citiesPopupService: CitiesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.citiesPopupService
                    .open(CitiesDialogComponent, params['id']);
            } else {
                this.modalRef = this.citiesPopupService
                    .open(CitiesDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
