import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { PostAddress } from './post-address.model';
import { PostAddressService } from './post-address.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-post-address',
    templateUrl: './post-address.component.html'
})
export class PostAddressComponent implements OnInit, OnDestroy {
postAddresses: PostAddress[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private postAddressService: PostAddressService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.postAddressService.query().subscribe(
            (res: ResponseWrapper) => {
                this.postAddresses = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInPostAddresses();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: PostAddress) {
        return item.id;
    }
    registerChangeInPostAddresses() {
        this.eventSubscriber = this.eventManager.subscribe('postAddressListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
