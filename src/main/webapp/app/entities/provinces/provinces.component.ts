import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { Provinces } from './provinces.model';
import { ProvincesService } from './provinces.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-provinces',
    templateUrl: './provinces.component.html'
})
export class ProvincesComponent implements OnInit, OnDestroy {
provinces: Provinces[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private provincesService: ProvincesService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.provincesService.query().subscribe(
            (res: ResponseWrapper) => {
                this.provinces = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInProvinces();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Provinces) {
        return item.id;
    }
    registerChangeInProvinces() {
        this.eventSubscriber = this.eventManager.subscribe('provincesListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
