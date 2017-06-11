import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { NearepTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PostAddressDetailComponent } from '../../../../../../main/webapp/app/entities/post-address/post-address-detail.component';
import { PostAddressService } from '../../../../../../main/webapp/app/entities/post-address/post-address.service';
import { PostAddress } from '../../../../../../main/webapp/app/entities/post-address/post-address.model';

describe('Component Tests', () => {

    describe('PostAddress Management Detail Component', () => {
        let comp: PostAddressDetailComponent;
        let fixture: ComponentFixture<PostAddressDetailComponent>;
        let service: PostAddressService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [NearepTestModule],
                declarations: [PostAddressDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PostAddressService,
                    EventManager
                ]
            }).overrideTemplate(PostAddressDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PostAddressDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PostAddressService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new PostAddress(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.postAddress).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
