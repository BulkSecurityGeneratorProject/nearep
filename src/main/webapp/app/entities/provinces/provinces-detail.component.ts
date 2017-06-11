import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Provinces } from './provinces.model';
import { ProvincesService } from './provinces.service';

@Component({
    selector: 'jhi-provinces-detail',
    templateUrl: './provinces-detail.component.html'
})
export class ProvincesDetailComponent implements OnInit, OnDestroy {

    provinces: Provinces;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private provincesService: ProvincesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProvinces();
    }

    load(id) {
        this.provincesService.find(id).subscribe((provinces) => {
            this.provinces = provinces;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProvinces() {
        this.eventSubscriber = this.eventManager.subscribe(
            'provincesListModification',
            (response) => this.load(this.provinces.id)
        );
    }
}
