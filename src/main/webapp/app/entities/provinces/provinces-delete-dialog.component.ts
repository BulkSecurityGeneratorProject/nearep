import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { Provinces } from './provinces.model';
import { ProvincesPopupService } from './provinces-popup.service';
import { ProvincesService } from './provinces.service';

@Component({
    selector: 'jhi-provinces-delete-dialog',
    templateUrl: './provinces-delete-dialog.component.html'
})
export class ProvincesDeleteDialogComponent {

    provinces: Provinces;

    constructor(
        private provincesService: ProvincesService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.provincesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'provincesListModification',
                content: 'Deleted an provinces'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('nearepApp.provinces.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-provinces-delete-popup',
    template: ''
})
export class ProvincesDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private provincesPopupService: ProvincesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.provincesPopupService
                .open(ProvincesDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
