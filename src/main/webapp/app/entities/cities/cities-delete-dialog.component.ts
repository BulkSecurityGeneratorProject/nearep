import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Cities } from './cities.model';
import { CitiesPopupService } from './cities-popup.service';
import { CitiesService } from './cities.service';

@Component({
    selector: 'jhi-cities-delete-dialog',
    templateUrl: './cities-delete-dialog.component.html'
})
export class CitiesDeleteDialogComponent {

    cities: Cities;

    constructor(
        private citiesService: CitiesService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.citiesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'citiesListModification',
                content: 'Deleted an cities'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('nearepApp.cities.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-cities-delete-popup',
    template: ''
})
export class CitiesDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private citiesPopupService: CitiesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.citiesPopupService
                .open(CitiesDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
