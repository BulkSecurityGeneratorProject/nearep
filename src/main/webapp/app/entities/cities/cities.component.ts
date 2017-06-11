import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager, ParseLinks, PaginationUtil, JhiLanguageService, AlertService } from 'ng-jhipster';

import { Cities } from './cities.model';
import { CitiesService } from './cities.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-cities',
    templateUrl: './cities.component.html'
})
export class CitiesComponent implements OnInit, OnDestroy {
cities: Cities[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private citiesService: CitiesService,
        private alertService: AlertService,
        private eventManager: EventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.citiesService.query().subscribe(
            (res: ResponseWrapper) => {
                this.cities = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCities();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Cities) {
        return item.id;
    }
    registerChangeInCities() {
        this.eventSubscriber = this.eventManager.subscribe('citiesListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
