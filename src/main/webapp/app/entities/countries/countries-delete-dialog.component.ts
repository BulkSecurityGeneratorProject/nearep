import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Countries } from './countries.model';
import { CountriesPopupService } from './countries-popup.service';
import { CountriesService } from './countries.service';

@Component({
    selector: 'jhi-countries-delete-dialog',
    templateUrl: './countries-delete-dialog.component.html'
})
export class CountriesDeleteDialogComponent {

    countries: Countries;

    constructor(
        private countriesService: CountriesService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.countriesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'countriesListModification',
                content: 'Deleted an countries'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('nearepApp.countries.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-countries-delete-popup',
    template: ''
})
export class CountriesDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private countriesPopupService: CountriesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.countriesPopupService
                .open(CountriesDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
