import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { Districts } from './districts.model';
import { DistrictsService } from './districts.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-districts',
    templateUrl: './districts.component.html'
})
export class DistrictsComponent implements OnInit, OnDestroy {
districts: Districts[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private districtsService: DistrictsService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.districtsService.query().subscribe(
            (res: ResponseWrapper) => {
                this.districts = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInDistricts();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Districts) {
        return item.id;
    }
    registerChangeInDistricts() {
        this.eventSubscriber = this.eventManager.subscribe('districtsListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
