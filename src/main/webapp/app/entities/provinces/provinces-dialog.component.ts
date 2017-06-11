import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Provinces } from './provinces.model';
import { ProvincesPopupService } from './provinces-popup.service';
import { ProvincesService } from './provinces.service';
import { Countries, CountriesService } from '../countries';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-provinces-dialog',
    templateUrl: './provinces-dialog.component.html'
})
export class ProvincesDialogComponent implements OnInit {

    provinces: Provinces;
    authorities: any[];
    isSaving: boolean;

    countries: Countries[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private provincesService: ProvincesService,
        private countriesService: CountriesService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.countriesService.query()
            .subscribe((res: ResponseWrapper) => { this.countries = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.provinces.id !== undefined) {
            this.subscribeToSaveResponse(
                this.provincesService.update(this.provinces), false);
        } else {
            this.subscribeToSaveResponse(
                this.provincesService.create(this.provinces), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Provinces>, isCreated: boolean) {
        result.subscribe((res: Provinces) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Provinces, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'nearepApp.provinces.created'
            : 'nearepApp.provinces.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'provincesListModification', content: 'OK'});
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

    trackCountriesById(index: number, item: Countries) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-provinces-popup',
    template: ''
})
export class ProvincesPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private provincesPopupService: ProvincesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.provincesPopupService
                    .open(ProvincesDialogComponent, params['id']);
            } else {
                this.modalRef = this.provincesPopupService
                    .open(ProvincesDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
