import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Countries } from './countries.model';
import { CountriesPopupService } from './countries-popup.service';
import { CountriesService } from './countries.service';

@Component({
    selector: 'jhi-countries-dialog',
    templateUrl: './countries-dialog.component.html'
})
export class CountriesDialogComponent implements OnInit {

    countries: Countries;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private countriesService: CountriesService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.countries.id !== undefined) {
            this.subscribeToSaveResponse(
                this.countriesService.update(this.countries), false);
        } else {
            this.subscribeToSaveResponse(
                this.countriesService.create(this.countries), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Countries>, isCreated: boolean) {
        result.subscribe((res: Countries) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Countries, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'nearepApp.countries.created'
            : 'nearepApp.countries.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'countriesListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-countries-popup',
    template: ''
})
export class CountriesPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private countriesPopupService: CountriesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.countriesPopupService
                    .open(CountriesDialogComponent, params['id']);
            } else {
                this.modalRef = this.countriesPopupService
                    .open(CountriesDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
