import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { PostAddress } from './post-address.model';
import { PostAddressPopupService } from './post-address-popup.service';
import { PostAddressService } from './post-address.service';

@Component({
    selector: 'jhi-post-address-delete-dialog',
    templateUrl: './post-address-delete-dialog.component.html'
})
export class PostAddressDeleteDialogComponent {

    postAddress: PostAddress;

    constructor(
        private postAddressService: PostAddressService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.postAddressService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'postAddressListModification',
                content: 'Deleted an postAddress'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('nearepApp.postAddress.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-post-address-delete-popup',
    template: ''
})
export class PostAddressDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private postAddressPopupService: PostAddressPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.postAddressPopupService
                .open(PostAddressDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
