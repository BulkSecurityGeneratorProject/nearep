import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Districts } from './districts.model';
import { DistrictsService } from './districts.service';

@Component({
    selector: 'jhi-districts-detail',
    templateUrl: './districts-detail.component.html'
})
export class DistrictsDetailComponent implements OnInit, OnDestroy {

    districts: Districts;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private districtsService: DistrictsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDistricts();
    }

    load(id) {
        this.districtsService.find(id).subscribe((districts) => {
            this.districts = districts;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDistricts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'districtsListModification',
            (response) => this.load(this.districts.id)
        );
    }
}
