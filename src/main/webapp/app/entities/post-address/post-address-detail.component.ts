import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { PostAddress } from './post-address.model';
import { PostAddressService } from './post-address.service';

@Component({
    selector: 'jhi-post-address-detail',
    templateUrl: './post-address-detail.component.html'
})
export class PostAddressDetailComponent implements OnInit, OnDestroy {

    postAddress: PostAddress;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private postAddressService: PostAddressService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPostAddresses();
    }

    load(id) {
        this.postAddressService.find(id).subscribe((postAddress) => {
            this.postAddress = postAddress;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPostAddresses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'postAddressListModification',
            (response) => this.load(this.postAddress.id)
        );
    }
}
