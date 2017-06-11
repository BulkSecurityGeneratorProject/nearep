import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Districts } from './districts.model';
import { DistrictsPopupService } from './districts-popup.service';
import { DistrictsService } from './districts.service';

@Component({
    selector: 'jhi-districts-delete-dialog',
    templateUrl: './districts-delete-dialog.component.html'
})
export class DistrictsDeleteDialogComponent {

    districts: Districts;

    constructor(
        private districtsService: DistrictsService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.districtsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'districtsListModification',
                content: 'Deleted an districts'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('nearepApp.districts.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-districts-delete-popup',
    template: ''
})
export class DistrictsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private districtsPopupService: DistrictsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.districtsPopupService
                .open(DistrictsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
