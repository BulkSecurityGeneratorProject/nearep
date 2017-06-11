import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Countries } from './countries.model';
import { CountriesService } from './countries.service';

@Component({
    selector: 'jhi-countries-detail',
    templateUrl: './countries-detail.component.html'
})
export class CountriesDetailComponent implements OnInit, OnDestroy {

    countries: Countries;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private countriesService: CountriesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCountries();
    }

    load(id) {
        this.countriesService.find(id).subscribe((countries) => {
            this.countries = countries;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCountries() {
        this.eventSubscriber = this.eventManager.subscribe(
            'countriesListModification',
            (response) => this.load(this.countries.id)
        );
    }
}
