import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Cities } from './cities.model';
import { CitiesService } from './cities.service';

@Component({
    selector: 'jhi-cities-detail',
    templateUrl: './cities-detail.component.html'
})
export class CitiesDetailComponent implements OnInit, OnDestroy {

    cities: Cities;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private citiesService: CitiesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCities();
    }

    load(id) {
        this.citiesService.find(id).subscribe((cities) => {
            this.cities = cities;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCities() {
        this.eventSubscriber = this.eventManager.subscribe(
            'citiesListModification',
            (response) => this.load(this.cities.id)
        );
    }
}
